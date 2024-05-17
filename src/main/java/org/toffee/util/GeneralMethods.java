package org.toffee.util;

import java.util.Scanner;
import java.util.Vector;

public class GeneralMethods {
  public static String GetStringInput(String prompt) {
    Scanner in = new Scanner(System.in);
    System.out.println(prompt);
    return in.nextLine();
  }

  public static int GetIntInput(String prompt) {
    Scanner in = new Scanner(System.in);
    System.out.println(prompt);
    return in.nextInt();
  }

  public static double GetDoubleInput(String prompt) {
    Scanner in = new Scanner(System.in);
    System.out.println(prompt);
    return in.nextDouble();
  }

  public static boolean GetBooleanInput(String prompt) {
    Scanner in = new Scanner(System.in);
    char c;
    do {
      System.out.println(prompt + " (y/n)");
      c = Character.toLowerCase(in.nextLine().charAt(0));
    } while (c != 'y' && c != 'n');
    return c == 'y';
  }

  public static char GetCharInput(String prompt) {
    Scanner in = new Scanner(System.in);
    System.out.println(prompt);
    return in.nextLine().charAt(0);
  }

  public static void PrintMenu(Vector<String> menu) {
    System.out.println("==================================");
    for (int i = 0; i < menu.size(); i++) {
      System.out.println(menu.get(i));
    }
    System.out.println("==================================");
  }

}
