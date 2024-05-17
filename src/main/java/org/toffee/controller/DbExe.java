<<<<<<<< HEAD:src/db/DbExe.java
package db;
========
package org.toffee.controller;
>>>>>>>> develop:src/main/java/org/toffee/controller/DbExe.java

import java.sql.*;
import java.util.Vector;

public class DbExe {
  private Connection conn;
  public DbExe() {
    this.conn = null;
  }

  private void connect() throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite:db/data.db");
  }

  private void disconnect() throws SQLException {
    conn.close();
  }

  /** Executes a DDL query
   * @param query SQL query to execute
   * @return true if successful, false otherwise
   */
  public boolean ddlExe(String query) {
    try {
      connect();
      Statement stmt = conn.createStatement();
      stmt.execute(query);
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    } finally {
      try { disconnect(); }
      catch (SQLException e) { System.out.println(e.getMessage()); }
    }
  }

  /** Executes a DML query and returns the results as a table
   * @param query SQL query to execute
   * @return Vector<Vector<String>> table of results
   */
  public Vector<Vector<String>> dmlExe(String query) {
    Vector<Vector<String>> table = new Vector<>();
    try {
      connect();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next()) {
        Vector<String> row = new Vector<>();
        for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
          row.add(rs.getString(i));
        }
        table.add(row);
      }
    } catch (SQLException e) {
      table.clear();
      System.out.println(e.getMessage());
    } finally {
      try { disconnect(); }
      catch (SQLException e) { System.out.println(e.getMessage()); }
    }
    return table;
  }

  public static void main(String[] args) {
    DbExe db = new DbExe();
    Vector<Vector<String>> vec = db.dmlExe("SELECT * FROM Customer;");
    for (Vector<String> row : vec) {
      for (String col : row) {
        System.out.print(col + " ");
      }
      System.out.println();
    }
  }
}
