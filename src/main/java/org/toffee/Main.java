package org.toffee;
import org.toffee.controller.CatalogMgr;
import org.toffee.model.Customer;
import org.toffee.controller.CustomersMgr;
import org.toffee.util.GeneralMethods;
import org.toffee.controller.OrdersMgr;
import org.toffee.controller.ShoppingCartMgr;

import java.util.Vector;
public class Main {
  private Customer customer_ = null;
  private final CustomersMgr customersMgr_ = new CustomersMgr();
  private final ShoppingCartMgr shoppingCartMgr_ = new ShoppingCartMgr();
  private final OrdersMgr ordersMgr_= new OrdersMgr();
  private int shoppingCartId_ = 0;
  private Vector<String> guestMenu_ = new Vector<String>() {{
    add("1- Login");
    add("2- Register");
    add("3- Catalog");
    add("4- Exit");
  }};
  private Vector<String> customerMenu_ = new Vector<String>() {{
    add("1- Catalog");
    add("2- Cart");
    add("3- Orders");
    add("4- Logout");
  }};
  private Vector<String> catalogMenu = new Vector<String>() {{
    add("1- List Items");
    add("2- List Categories");
    add("3- List Brands");
    add("4- Add Product to Cart");
    add("5- Back");
  }};
  private Vector<String> cartMenu = new Vector<String>() {{
    add("1- List Products");
    add("2- Remove Product from Cart");
    add("3- Checkout");
    add("4- Back");
  }};
  private Vector<String> ordersMenu = new Vector<String>() {{
    add("1- List Closed Orders");
    add("2- List Open Orders");
    add("3- Pay Order");
    add("4- ReOrder");
    add("5- Back");
  }};

  private int getChoice(Vector<String> menu) {
    int c;
    do {
      c = GeneralMethods.GetIntInput("Enter your choice: (1-" + menu.size() + ")");
    } while (c < 1 || c > menu.size());
    return c;
  }

  private void runCatalog() {
    CatalogMgr catalog = new CatalogMgr(shoppingCartId_);
    do {
      GeneralMethods.PrintMenu(catalogMenu);
      int c = getChoice(catalogMenu);
      if (c == 1) {
        catalog.listItems();
      } else if (c == 2) {
        catalog.listCategories();
      } else if (c == 3) {
        catalog.listBrands();
      } else if (c == 4) {
        catalog.selectItem();
      } else {
        break;
      }
    } while (true);
  }

  public void runCart() {
    do {
      GeneralMethods.PrintMenu(cartMenu);
      int c = getChoice(cartMenu);
      if (c == 1) {
        shoppingCartMgr_.listItemsIn(shoppingCartId_);
      } else if (c == 2) {
        shoppingCartMgr_.removeItemIn(shoppingCartId_);
      } else if (c == 3) {
        ordersMgr_.placeOrder(customer_, shoppingCartMgr_.getTotalPriceIn(shoppingCartId_), shoppingCartMgr_.getItemsIn(shoppingCartId_));
        shoppingCartMgr_.removeItemsIn(shoppingCartId_);
      } else {
        break;
      }
    } while (true);
  }

  public void runOrders() {
    do {
      GeneralMethods.PrintMenu(ordersMenu);
      int c = getChoice(ordersMenu);
      if (c == 1) {
        ordersMgr_.listOrders(customer_.GetId(), false);
      } else if (c == 2) {
        ordersMgr_.listOrders(customer_.GetId(), true);
      } else if (c == 3) {
        ordersMgr_.payOrder(customer_);
      } else if (c == 4) {
        ordersMgr_.reOrder(customer_.GetId());
      } else {
        break;
      }
    } while (true);
  }

  public void run() {
    shoppingCartId_ = shoppingCartMgr_.addShoppingCart(0);
    while (true) {
      if (customer_ == null) {
        GeneralMethods.PrintMenu(guestMenu_);
        int c = getChoice(guestMenu_);
        if (c == 1) {
          customer_ = customersMgr_.accessCustomer();
          if (customer_ != null) {
            shoppingCartMgr_.deleteShoppingCartWithCustomerId(0);
            shoppingCartId_ = shoppingCartMgr_.getShoppingCartId(customer_.GetId());
          }
        } else if (c == 2) {
          customer_ = customersMgr_.addCustomer();
          if (customer_ != null) {
            shoppingCartMgr_.assignCustomerId(shoppingCartId_, customer_.GetId());
          }
        } else if (c == 3) {
          runCatalog();
        } else {
          shoppingCartMgr_.deleteShoppingCartWithCustomerId(0);
          System.exit(0);
        }
      } else {
        GeneralMethods.PrintMenu(customerMenu_);
        int c = getChoice(customerMenu_);
        if (c == 1) {
          runCatalog();
        } else if (c == 2) {
          runCart();
        } else if (c == 3) {
          runOrders();
        } else {
          customer_ = null;
          shoppingCartId_ = shoppingCartMgr_.addShoppingCart(0);
        }
      }
    }
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.run();
  }
}
