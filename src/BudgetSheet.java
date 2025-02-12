import java.sql.*;
import java.util.ArrayList;
import java.math.BigDecimal;

class BudgetSheet {
  int id = -1;
  String name = "";
  public ArrayList<Transaction> transactions = new ArrayList<>();

  public BudgetSheet(int id, String name) {
    this.id = id;
    this.name = name;

    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT id FROM transactions WHERE budget_sheet_id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        transactions.add(new Transaction(rs.getInt("id")));
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public BudgetSheet(int id) {
    this.id = id;

    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT name FROM budgetsheet WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        this.name = rs.getString("name");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public BudgetSheet(String name) {
    this.name = name;
  }

  public boolean save() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");

      PreparedStatement test = conn.prepareStatement(
          "SELECT * FROM budgetsheet WHERE id = ?");
      test.setInt(1, id);

      ResultSet rs = test.executeQuery();

      PreparedStatement stmt = null;
      if (rs.next()) {
        stmt = conn.prepareStatement(
            "UPDATE budgetsheet SET name=? WHERE id=?");

        stmt.setInt(2, id);
      } else {
        stmt = conn.prepareStatement(
            "INSERT INTO budgetsheet (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      }
      stmt.setString(1, name);

      int updated = stmt.executeUpdate();

      ResultSet keys = stmt.getGeneratedKeys();
      if (keys.next()) {
        id = keys.getInt(1);
      }

      stmt.close();
      conn.close();

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
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM budgetsheet WHERE id = ?");
      if (id != 0) {
        stmt.setInt(1, id);
        stmt.executeUpdate();
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
      PreparedStatement stmt = conn
          .prepareStatement("SELECT name FROM budgetsheet WHERE id = ?");
      stmt.setInt(1, id_num);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        bs = new BudgetSheet(id_num, rs.getString("name"));
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

  public static ArrayList<BudgetSheet> find(String name) {
    ArrayList<BudgetSheet> budgets = new ArrayList<BudgetSheet>();
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM budgetsheet WHERE name = ?");
      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {

        budgets.add(new BudgetSheet(rs.getInt("id"), rs.getString("name")));
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

  public BigDecimal getTotalAmount(String type) {
    BigDecimal total = BigDecimal.ZERO;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn
          .prepareStatement("SELECT SUM(amount) FROM transactions WHERE budget_sheet_id = ? AND type = ?");
      stmt.setInt(1, id);
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
// public static ArrayList<BudgetSheet> find(String budgetSheetName) {
// ArrayList<BudgetSheet> budgets = new ArrayList<>();
// try {
// Connection conn = DriverManager.getConnection("jdbc:postgresql:budget",
// "postgres", "Qu!stors2022");
// PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions
// WHERE budget_sheet_name = ?");
// stmt.setString(1, budgetSheetName);
// ResultSet rs = stmt.executeQuery();
//
// while (rs.next()) {
//
// budgets.add(new BudgetSheet(
//
// rs.getString("type"),
// rs.getString("name"),
// rs.getBigDecimal("amount"),
// rs.getString("priority"),
//
//
// rs.getString("budget_sheet_name")
// ));
// }
//
// rs.close();
// stmt.close();
// conn.close();
// } catch (SQLException ex) {
// ex.printStackTrace();
// System.exit(1);
// }
// return budgets;
// }
//
//
//
// public String getBudgetSheetName() {
// return budgetSheetName;
// }
//
// public void setBudgetSheetName(String budgetSheetName) {
// this.budgetSheetName = budgetSheetName;
// }
//
//
// public String getType() {
// return type;
// }
//
// public void setType(String type) {
// this.type = type;
// }
//
// public String getTitle() {
// return title;
// }
//
// public void setTitle(String title) {
// this.title = title;
// }
//
// public BigDecimal getAmount() {
// return amount;
// }
//
// public void setAmount(BigDecimal amount) {
// this.amount = amount;
// }
//
// public String getPriority() {
// return priority;
// }
//
// public void setPriority(String priority) {
// this.priority = priority;
// }
// }
//
//
//
//
//
