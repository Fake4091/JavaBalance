import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

class BudgetSheet {
  int id = -1;
  String type = "";
  String title = "";
  BigDecimal amount = BigDecimal.ZERO;


  public BudgetSheet(String type, String title, BigDecimal amount) {
    this.type = type;
    this.title = title;
    this.amount = amount;
  }


  public BudgetSheet(int id) {
    this.id = id;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT type, name, amount FROM transactions WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        this.type = rs.getString(1);
        this.title = rs.getString(2);
        this.amount = BigDecimal.valueOf(rs.getDouble(3));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }


  public BudgetSheet(int id, String type, String title, BigDecimal amount) {
    this.id = id;
    this.type = type;
    this.title = title;
    this.amount = amount;
  }


  public boolean save() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");

      // Insert new record for this budget sheet (type, title, amount)
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (type, name, amount) VALUES (?, ?, ?)");
      stmt.setString(1, type);
      stmt.setString(2, title);
      stmt.setBigDecimal(3, amount); // Use BigDecimal for precise money handling

      int updated = stmt.executeUpdate();
      return updated == 1;
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return false;
  }


  public void delete() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM transactions WHERE id = ?");
      if (id != 0) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
        id = -1;
        title = null;
        type = null;
        amount = BigDecimal.ZERO; // Reset amount to zero
      }
      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }


  public static BudgetSheet find(int id_num) {
    BudgetSheet bs = null;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE id = ?");
      stmt.setInt(1, id_num);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        bs = new BudgetSheet(rs.getInt("id"), rs.getString("type"), rs.getString("name"),
                BigDecimal.valueOf(rs.getDouble("amount"))); // Convert to BigDecimal
      }

      rs.close();
      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return bs;
  }


  public static ArrayList<BudgetSheet> find(String title) {
    ArrayList<BudgetSheet> budgets = new ArrayList<BudgetSheet>();
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE name = ?");
      stmt.setString(1, title);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        budgets.add(new BudgetSheet(rs.getInt("id"), rs.getString("type"), rs.getString("name"),
                BigDecimal.valueOf(rs.getDouble("amount")))); // Updated 'title' to 'name'
      }

      rs.close();
      stmt.close();
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return budgets;
  }


  public static BigDecimal getTotalAmount(String title, String type) {
    BigDecimal total = BigDecimal.ZERO;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE name = ? AND type = ?");
      stmt.setString(1, title);
      stmt.setString(2, type);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        total = rs.getBigDecimal(1); // Get the summed amount
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


  public boolean addField(String type, String title, BigDecimal amount) {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");


      PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (type, name, amount) VALUES (?, ?, ?)");
      stmt.setString(1, type);
      stmt.setString(2, title);
      stmt.setBigDecimal(3, amount);

      int updated = stmt.executeUpdate();

      stmt.close();
      conn.close();

      return updated == 1;
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}


