package org.toffee.model;

public class ShoppingCart {
  private final int id_;
  private final int customerId_;
  ShoppingCart(int customerId) {
    id_ = 0;
    customerId_ = customerId;
  }
}
