package org.toffee.controller;

import org.toffee.util.GeneralMethods;
import org.toffee.util.MailService;
import org.toffee.model.Customer;

import java.util.Vector;

public class CustomersMgr {

  public Customer addCustomer() {
    String name = GeneralMethods.GetStringInput("Enter name:");
    String email = GeneralMethods.GetStringInput("Enter email:");
    String password = GeneralMethods.GetStringInput("Enter password:");
    String address = GeneralMethods.GetStringInput("Enter address:");
    DbExe db = new DbExe();
    boolean isEmailTaken = db.dmlExe("SELECT * FROM Customer WHERE email = '" + email + "';").size() != 0;
    if (isEmailTaken) {
      System.out.println("Email is already taken.");
      return null;
    } else if (!email.contains("@")) {
      System.out.println("Invalid email.");
      return null;
    }
    if (password.length() < 8) {
      System.out.println("Password must be at least 8 characters long.");
      return null;
    }
    MailService ms = new MailService();
    String otp = ms.sendOTP(email, "Welcome to Toffee!");
    String enteredOtp = GeneralMethods.GetStringInput("Enter OTP:");
    int cnt = 2;
    while (cnt-- != 0 && !otp.equals(enteredOtp)) {
      enteredOtp = GeneralMethods.GetStringInput("invalid OTP. Enter again:");
    }
    if (otp.equals(enteredOtp)) {
      System.out.println("OTP verified successfully.");
      boolean isInserted = db.ddlExe("INSERT INTO Customer (name, email, password, address)" +
          "VALUES ('" + name + "', '" + email + "', '" + password + "', '" + address + "');");
      if (isInserted) {
        System.out.println("Customer added successfully.");
        ms.sendEmail(email, "Welcome to Toffee!", "Your registration completed!\nNow you can buy our products as you want!\n");
        return new Customer(Integer.parseInt(db.dmlExe("SELECT max(id) FROM Customer;").get(0).get(0)), name, email, password, address);
      } else {
        System.out.println("Error: Failed to add customer. Please try again.");
        return null;
      }
    } else {
      System.out.println("Failed to verify OTP.");
      System.out.println("Customer not added.");
      return null;
    }
  }

  public Customer accessCustomer() {
    String email = GeneralMethods.GetStringInput("Enter email:");
    String password = GeneralMethods.GetStringInput("Enter password:");
    DbExe db = new DbExe();
    System.out.println("Getting customer info...");
    Vector<Vector<String>> vec = db.dmlExe("SELECT * FROM Customer " +
        "WHERE email = '" +  email + "' AND password = '" + password + "';");
    if (vec.size() == 0) {
      System.out.println("Invalid email or password.");
      return null;
    } else {
      Vector<String> cInfo = vec.get(0);
      System.out.println("Welcome, " + cInfo.get(1) + "!");
      return new Customer(Integer.parseInt(cInfo.get(0)), cInfo.get(1),
          cInfo.get(2), cInfo.get(3), cInfo.get(4));
    }
  }

}
