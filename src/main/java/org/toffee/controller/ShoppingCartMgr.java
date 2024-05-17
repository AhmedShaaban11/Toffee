package org.toffee.controller;

import org.toffee.model.ShippedItem;
import org.toffee.util.GeneralMethods;

import java.util.Vector;

public class ShoppingCartMgr {

  public int addShoppingCart(int customerId) {
    DbExe db = new DbExe();
    db.ddlExe("INSERT INTO ShoppingCart (customerId) VALUES (" + (customerId == 0 ? "NULL" : customerId) + ");");
    return Integer.parseInt(db.dmlExe("SELECT max(id) FROM ShoppingCart;").get(0).get(0));
  }

  public boolean assignCustomerId(int shoppingCartId, int customerId) {
    DbExe db = new DbExe();
    return db.ddlExe("UPDATE ShoppingCart SET customerId = " + customerId + " WHERE id = " + shoppingCartId + ";");
  }

  public boolean deleteShoppingCartWithCustomerId(int customerId) {
    DbExe db = new DbExe();
    Vector<Vector<String>> vec = db.dmlExe("Select id FROM ShoppingCart WHERE customerId " + (customerId == 0 ? "IS NULL" : "= " + customerId) + ";");
    if (vec.size() == 0) {
      System.out.println("No such shopping cart.");
      return false;
    }
    int shoppingCartId = Integer.parseInt(vec.get(0).get(0));
    db.ddlExe("DELETE FROM ShoppingCartItem WHERE shoppingCartId = " + shoppingCartId + ";");
    return db.ddlExe("DELETE FROM ShoppingCart WHERE id = " + shoppingCartId + ";");
  }

  public void listItemsIn(int shoppingCartId) {
    DbExe db = new DbExe();
    Vector<Vector<String>> items = db.dmlExe("SELECT p.id, p.name, quantity, unitPrice FROM ShoppingCartItem INNER JOIN Product AS p ON productId = p.id WHERE shoppingCartId = " + shoppingCartId + ";");
    if (items.size() == 0) {
      System.out.println("No such shopping cart.");
      return;
    }
    System.out.println("productId productName quantity unitPrice");
    for (Vector<String> row : items) {
      for (String col : row) {
        System.out.print(col + " ");
      }
      System.out.println();
    }
  }

  public boolean removeItemIn(int shoppingCartId) {
    int productId = GeneralMethods.GetIntInput("Enter product id:");
    DbExe db = new DbExe();
    Vector<Vector<String>> items = db.dmlExe("SELECT * FROM ShoppingCartItem WHERE shoppingCartId = " + shoppingCartId + " AND productId = " + productId + ";");
    if (items.size() == 0) {
      System.out.println("No such item in shopping cart.");
      return false;
    }
    Vector<String> item = items.get(0);
    double quantityRemoved = GeneralMethods.GetDoubleInput("Enter quantity:");
    if (db.dmlExe("Select isSealed FROM Product WHERE id = " + productId + ";").get(0).get(0).equals("1")) {
      quantityRemoved = Math.floor(quantityRemoved);
    }
    if (quantityRemoved > Double.parseDouble(item.get(2))) {
      System.out.println("Quantity exceeds the amount in shopping cart.");
      return false;
    } else if (quantityRemoved == Double.parseDouble(item.get(2))) {
      return db.ddlExe("DELETE FROM ShoppingCartItem WHERE shoppingCartId = " + shoppingCartId + " AND productId = " + productId + ";");
    } else {
      return db.ddlExe("UPDATE ShoppingCartItem SET quantity = quantity - " + quantityRemoved + " WHERE shoppingCartId = " + shoppingCartId + " AND productId = " + productId + ";");
    }
  }

  public double getTotalPriceIn(int shoppingCartId) {
    DbExe db = new DbExe();
    Vector<Vector<String>> items = db.dmlExe("SELECT SUM(quantity * unitPrice) FROM ShoppingCartItem WHERE shoppingCartId = " + shoppingCartId + ";");
    if (items.size() == 0) {
      System.out.println("No such shopping cart.");
      return 0;
    }
    return Double.parseDouble(items.get(0).get(0));
  }

  public Vector<ShippedItem> getItemsIn(int shoppingCartId) {
    DbExe db = new DbExe();
    Vector<Vector<String>> items = db.dmlExe("SELECT productId, quantity, unitPrice FROM ShoppingCartItem WHERE shoppingCartId = " + shoppingCartId + ";");
    Vector<ShippedItem> shippedItems = new Vector<>();
    for (Vector<String> item : items) {
      shippedItems.add(new ShippedItem(Integer.parseInt(item.get(0)), Double.parseDouble(item.get(1)), Double.parseDouble(item.get(2))));
    }
    return shippedItems;
  }

  public boolean removeItemsIn(int shoppingCartId) {
    DbExe db = new DbExe();
    return db.ddlExe("DELETE FROM ShoppingCartItem WHERE shoppingCartId = " + shoppingCartId + ";");
  }

  public int getShoppingCartId(int customerId) {
    DbExe db = new DbExe();
    Vector<Vector<String>> items = db.dmlExe("SELECT id FROM ShoppingCart WHERE customerId = " + customerId + ";");
    if (items.size() == 0) {
      System.out.println("No such shopping cart.");
      return 0;
    }
    return Integer.parseInt(items.get(0).get(0));
  }

  public static void main(String[] args) {
    ShoppingCartMgr shoppingCartMgr = new ShoppingCartMgr();
    System.out.println(shoppingCartMgr.getShoppingCartId(1));
  }
}
