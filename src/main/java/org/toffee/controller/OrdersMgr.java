package org.toffee.controller;

import java.util.Vector;

import org.toffee.util.GeneralMethods;
import org.toffee.model.Customer;
import org.toffee.model.ShippedItem;

public class OrdersMgr {

  public void listOrders(int customerId, boolean isOpen) {
    DbExe db = new DbExe();
    Vector<Vector<String>> orders = db.dmlExe("SELECT * FROM 'Order' WHERE customerId = " + customerId + " And isOpen = " + isOpen + ";");
    if (orders.size() == 0) {
      System.out.println("No such order.");
      return;
    }
    System.out.println("orderId customerId deliveryAddress totalPrice orderDate isOpen");
    for (Vector<String> row : orders) {
      for (String col : row) {
        System.out.print(col + " ");
      }
      System.out.println();
    }
  }

  public void placeOrder(Customer customer, double totalPrice, Vector<ShippedItem> items) {
    String shippingAddress = "";
    if (GeneralMethods.GetBooleanInput("Does shipping address match your billing address?")) {
      shippingAddress = customer.GetAddress();
    } else {
      shippingAddress = GeneralMethods.GetStringInput("Enter shipping address:");
    }
    DbExe db = new DbExe();
    db.ddlExe("INSERT INTO 'Order' (customerId, deliveryAddress, totalPrice) VALUES (" + customer.GetId() + ", '" + shippingAddress + "', " + totalPrice + ");");
    int oid = Integer.parseInt(db.dmlExe("SELECT max(id) FROM 'Order';").get(0).get(0));
    for (int i = 0; i < items.size(); i++) {
      db.ddlExe("INSERT INTO OrderItem (orderId, productId, quantity, unitPrice) VALUES (" + oid + ", " + items.get(i).productId() + ", " + items.get(i).quantity() + ", " + items.get(i).price() + ");");
    }
  }

  public double payOrder(Customer customer_) {
    int oid = GeneralMethods.GetIntInput("Enter order id:");
    DbExe db = new DbExe();
    Vector<Vector<String>> vec = db.dmlExe("SELECT id, customerId, totalPrice, isOpen FROM 'Order' WHERE id = " + oid + ";");
    if (vec.size() == 0) {
      System.out.println("Order not found.");
      return 0;
    }
    Vector<String> order = vec.get(0);
    if (!order.get(1).equals(String.valueOf(customer_.GetId()))) {
      System.out.println("Order doesn't belong to you.");
      return 0;
    }
    if (order.get(3).equals("0")) {
      System.out.println("Order already paid.");
      return 0;
    }
    double totalPrice = Double.parseDouble(order.get(2));
    double cashPaid = GeneralMethods.GetDoubleInput("Enter cash paid:");
    if (cashPaid < totalPrice) {
      System.out.println("Cash paid is less than total price.");
      return 0;
    }
    double change = cashPaid - totalPrice;
    System.out.println("Order paid! Change: " + change);
    db.ddlExe("UPDATE 'Order' SET isOpen = 0 WHERE id = " + oid + ";");
    return change;
  }

  public boolean reOrder(int customerId) {
    int oid = GeneralMethods.GetIntInput("Enter order id:");
    DbExe db = new DbExe();
    Vector<Vector<String>> vec = db.dmlExe("SELECT customerId, deliveryAddress, totalPrice FROM 'Order' WHERE id = " + oid + ";");
    if (vec.size() == 0) {
      System.out.println("Order not found.");
      return false;
    }
    Vector<String> order = vec.get(0);
    if (!order.get(0).equals(String.valueOf(customerId))) {
      System.out.println("Order doesn't belong to you.");
      return false;
    }
    if (!db.ddlExe("INSERT INTO 'Order' (customerId, deliveryAddress, totalPrice) VALUES (" + Integer.parseInt(order.get(0)) + ", '" + order.get(1) + "', " + Double.parseDouble(order.get(2)) + ");")) {
      System.out.println("Failed to re-order. Please try again later.");
      return false;
    }
    int newOid = Integer.parseInt(db.dmlExe("SELECT max(id) FROM 'Order';").get(0).get(0));
    Vector<Vector<String>> items = db.dmlExe("SELECT productId, quantity, unitPrice FROM OrderItem WHERE orderId = " + oid + ";");
    for (Vector<String> item : items) {
      db.ddlExe("INSERT INTO OrderItem (orderId, productId, quantity, unitPrice) VALUES (" + newOid + ", " + Integer.parseInt(item.get(0)) + ", " + Double.parseDouble(item.get(1)) + ", " + Double.parseDouble(item.get(2)) + ");");
    }
    System.out.println("Re-order successful!");
    return true;
  }

  public static void main(String[] args) {

  }
}
