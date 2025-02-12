import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Transaction {
  int id = -1;
  String type = "";
  String title = "";
  String priority = "";
  BigDecimal amount = BigDecimal.ZERO;
  int budgetSheetID = -1;

  public Transaction(int id, String type, String title, BigDecimal amount, String priority, int budgetSheetID) {
    this.id = id;
    this.type = type;
    this.title = title;
    this.amount = amount;
    this.priority = priority;
    this.budgetSheetID = budgetSheetID;
  }

  public Transaction(String type, String title, BigDecimal amount, String priority, int budgetSheetID) {
    this.type = type;
    this.title = title;
    this.amount = amount;
    this.priority = priority;
    this.budgetSheetID = budgetSheetID;
  }

  public Transaction(int id) {
    this.id = id;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn
          .prepareStatement("SELECT type, name, amount, priority, budget_sheet_ID FROM transactions WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        this.type = rs.getString(1);
        this.title = rs.getString(2);
        this.amount = BigDecimal.valueOf(rs.getDouble(3));
        this.priority = rs.getString(4);
        this.budgetSheetID = rs.getInt(5);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public boolean save() {
    if (type != "" && title != "" && amount != BigDecimal.ZERO && budgetSheetID != -1) {
      try {
        Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");

        PreparedStatement test = conn.prepareStatement(
            "SELECT * FROM transactions WHERE id = ?");
        test.setInt(1, id);

        ResultSet rs = test.executeQuery();

        PreparedStatement stmt = null;
        if (rs.next()) {
          stmt = conn.prepareStatement(
              "UPDATE transactions SET type=?, name=?, amount=?, priority=?, budget_sheet_id=? WHERE id=?");
        } else {
          stmt = conn.prepareStatement(
              "INSERT INTO transactions (type, name, amount, priority, budget_sheet_id) VALUES (?, ?, ?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS);
        }
        stmt.setString(1, type);
        stmt.setString(2, title);
        stmt.setBigDecimal(3, amount);
        stmt.setString(4, priority);
        stmt.setInt(5, budgetSheetID); // Add the budget sheet name to the insert statement

        int updated = stmt.executeUpdate();

        ResultSet keys = stmt.getGeneratedKeys();
        if (rs.next()) {
          id = keys.getInt(1);
        }

        stmt.close();
        conn.close();

        return updated == 1;
      } catch (SQLException ex) {
        ex.printStackTrace();
        System.exit(1);
      }
    }
    return false;
  }

  public void delete() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM transactions WHERE id = ?");
      stmt.setInt(1, id);
      stmt.executeUpdate();

      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public static Transaction find(int id_num) {
    Transaction t = null;

    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn
          .prepareStatement("SELECT type, name, amount, priority, budget_sheet_id FROM transactions WHERE id = ?");
      stmt.setInt(1, id_num);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        t = new Transaction(id_num, rs.getString("type"), rs.getString("name"), rs.getBigDecimal("amount"),
            rs.getString("priority"), rs.getInt("budget_sheet_id"));
      }

      rs.close();
      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return t;
  }

  public static ArrayList<Transaction> find(String name) {
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE name = ?");
      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {

        transactions.add(new Transaction(rs.getInt("id"), rs.getString("type"), rs.getString("name"),
            rs.getBigDecimal("amount"), rs.getString("priority"), rs.getInt("budget_sheet_id")));
      }

      rs.close();
      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return transactions;
  }

  public static BigDecimal getTotalAmount(int budgetSheetID, String type) {
    BigDecimal total = BigDecimal.ZERO;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn
          .prepareStatement("SELECT SUM(amount) FROM transactions WHERE budget_sheet_id = ? AND type = ?");
      stmt.setInt(1, budgetSheetID);
      stmt.setString(2, type);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        total = rs.getBigDecimal(1);
      }

      rs.close();
      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return total;
  }

}
