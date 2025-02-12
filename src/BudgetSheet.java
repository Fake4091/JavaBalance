import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;

class BudgetSheet {
  int id = -1;
  String type = "";
  String title = "";
  String priority = "";
  BigDecimal amount = BigDecimal.ZERO;
  String budgetSheetName = "";


  public BudgetSheet(String type, String title, BigDecimal amount, String priority, String budgetSheetName) {
    this.type = type;
    this.title = title;
    this.amount = amount;
    this.priority = priority;
    this.budgetSheetName = budgetSheetName;  // Initialize the new field
  }


  public BudgetSheet(int id) {
    this.id = id;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT type, name, amount, priority, budget_sheet_name FROM transactions WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        this.type = rs.getString(1);
        this.title = rs.getString(2);
        this.amount = BigDecimal.valueOf(rs.getDouble(3));
        this.priority = rs.getString(4);
        this.budgetSheetName = rs.getString(5);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }


  public boolean save() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement(
              "INSERT INTO transactions (type, name, amount, priority, budget_sheet_name) VALUES (?, ?, ?, ?, ?)"
      );
      stmt.setString(1, type);
      stmt.setString(2, title);
      stmt.setBigDecimal(3, amount);
      stmt.setString(4, priority);
      stmt.setString(5, budgetSheetName);  // Add the budget sheet name to the insert statement

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
        amount = BigDecimal.ZERO;
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

        bs = new BudgetSheet(rs.getInt("id"));

        bs.type = rs.getString("type");
        bs.title = rs.getString("name");
        bs.amount = rs.getBigDecimal("amount");
        bs.priority = rs.getString("priority");
        bs.budgetSheetName = rs.getString("budget_sheet_name");
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




  public static ArrayList<BudgetSheet> find(String budgetSheetName) {
    ArrayList<BudgetSheet> budgets = new ArrayList<>();
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE budget_sheet_name = ?");
      stmt.setString(1, budgetSheetName);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {

        budgets.add(new BudgetSheet(

                rs.getString("type"),
                rs.getString("name"),
                rs.getBigDecimal("amount"),
                rs.getString("priority"),


                rs.getString("budget_sheet_name")
        ));
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



  public static BigDecimal getTotalAmount(String budgetSheetName, String type) {
    BigDecimal total = BigDecimal.ZERO;
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "Qu!stors2022");
      PreparedStatement stmt = conn.prepareStatement("SELECT SUM(amount) FROM transactions WHERE budget_sheet_name = ? AND type = ?");
      stmt.setString(1, budgetSheetName);
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


  public boolean addField(String type, String title, BigDecimal amount, String priority, String budgetSheetName) {
    try {
      Connection conn = DriverManager.getConnection("jdbc:postgresql:budget", "postgres", "password");
      PreparedStatement stmt;

      if ("expenses".equals(type)) {
        stmt = conn.prepareStatement("INSERT INTO transactions (type, name, amount, priority, budget_sheet_name) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(4, priority);
      } else {
        stmt = conn.prepareStatement("INSERT INTO transactions (type, name, amount, budget_sheet_name) VALUES (?, ?, ?, ?)");
      }

      stmt.setString(1, type);
      stmt.setString(2, title);
      stmt.setBigDecimal(3, amount);
      stmt.setString(5, budgetSheetName);

      int updated = stmt.executeUpdate();
      stmt.close();
      conn.close();

      return updated == 1;
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false;
    }
  }


  public String getBudgetSheetName() {
    return budgetSheetName;
  }

  public void setBudgetSheetName(String budgetSheetName) {
    this.budgetSheetName = budgetSheetName;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }
}





