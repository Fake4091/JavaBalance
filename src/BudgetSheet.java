import java.sql.*;
import java.util.*;

class BudgetSheet {
  int id = -1;
  String type = "";
  String title = "";
  float amount = (float) -1.0;

  public BudgetSheet(int id) {
    this.id = id;

    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget");
      PreparedStatement stmt = conn.prepareStatement("SELECT type, name, amount FROM transactions WHERE id = ?");

      stmt.setInt(1, id);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        this.type = rs.getString(1);
        this.title = rs.getString(2);
        this.amount = (float) rs.getDouble(3);
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }

  }

  public BudgetSheet(String type, String title, float amount) {
    this.type = type;
    this.title = title;
    this.amount = amount;
  }

  public BudgetSheet(int id, String type, String title, float amount) {
    this.id = id;
    this.type = type;
    this.title = title;
    this.amount = amount;
  }

  public boolean save() {

    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget");

      PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM transactions WHERE id = ?");

      selectStmt.setInt(1, id);

      ResultSet rs = selectStmt.executeQuery();

      if (rs.next()) {
        selectStmt = conn.prepareStatement("UPDATE transactions SET type = ?, name = ?, amount = ? WHERE id = ?");
        selectStmt.setString(1, type);
        selectStmt.setString(2, title);
        selectStmt.setFloat(3, amount);

        int updated = selectStmt.executeUpdate();

        return (updated == 1);
      } else {

        PreparedStatement stmt = conn
            .prepareStatement("INSERT INTO transactions (type, name, amount) VALUES (?, ?, ?)");

        stmt.setString(1, type);
        stmt.setString(2, title);
        stmt.setFloat(3, amount);

        int updated = stmt.executeUpdate();

        return updated == 1;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }

    return false;
  }

  public void delete() {
    try {
      Connection connection = DriverManager.getConnection("jdbc:postgresql:budget");
      PreparedStatement stmt = connection.prepareStatement("DELETE FROM transactions WHERE id = ?");

      if (id != 0) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
        id = -1;
        title = null;
        type = null;
        amount = -1;
      }

      stmt.close();
      connection.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public static BudgetSheet find(int id_num) {
    BudgetSheet bs = null;

    try {
      Connection connection = DriverManager.getConnection("jdbc:postgresql:budget");
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM transactions WHERE id = ?");

      stmt.setInt(1, id_num);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        bs = new BudgetSheet(rs.getInt("id"), rs.getString("type"), rs.getString("title"),
            (float) rs.getDouble("amount"));
      }

      rs.close();
      stmt.close();
      connection.close();

    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return bs;
  }

  public static ArrayList<BudgetSheet> find(String title) {
    ArrayList<BudgetSheet> budgets = new ArrayList<BudgetSheet>();

    try {
      Connection connection = DriverManager.getConnection("jdbc:postgresql:budget");
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM transactions WHERE title = ?");

      stmt.setString(1, title);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        budgets.add(new BudgetSheet(rs.getInt("id"), rs.getString("type"), rs.getString("title"),
            (float) rs.getDouble("amount")));
      }

      rs.close();
      stmt.close();
      connection.close();

    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    return budgets;
  }
}
