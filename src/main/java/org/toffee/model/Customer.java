package org.toffee.model;

public class Customer {
  private final int id_;
  private final String name_;
  private final String email_;
  private final String password_;
  private final String address_;

  public Customer(int id, String name, String email, String password, String address) {
    id_ = id;
    name_ = name;
    email_ = email;
    password_ = password;
    address_ = address;
  }

  public int GetId() { return id_; }
  public String GetAddress() { return address_; }

}
