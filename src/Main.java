import java.util.*;
import java.math.BigDecimal;

public class Main {
  final static Scanner input = new Scanner(System.in);
  private static List<BudgetSheet> allBudgetSheets = new ArrayList<>();

  public static void main(String[] args) {
    System.out.println("Welcome to JavaBalance");
    boolean hasExited = false;

    while (!hasExited) {
      System.out.print("Here are your options:\n");
      String userOptions = """
          1. Create A New Budget Sheet
          2. View My Budget Sheets
          3. Edit Budget Sheet Fields
          4. Help
          5. Exit Program
          """;
      System.out.print(userOptions);

      String optionString = input.nextLine();
      int option = 0;
      try {
        option = Integer.parseInt(optionString);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input, please enter a number.");
        continue;
      }

      switch (option) {
        case 1:
          createNewBudgetSheet();
          break;
        case 2:
          viewMyBudgetSheet();
          break;
        case 3:
          editBudgetSheetFields();
          break;
        case 4:
          help();
          break;
        case 5:
          hasExited = true;
          break;
        default:
          System.out.println("Invalid option");
          break;
      }
    }
  }

  public static void createNewBudgetSheet() {
    System.out.print("Enter budget sheet name (e.g., January Expenses): ");
    String budgetSheetName = input.nextLine();

    boolean addingFields = true;
    while (addingFields) {
      System.out.print("Enter the type (income/expenses): ");
      String type = input.nextLine();

      System.out.print("Enter the name of the field: ");
      String name = input.nextLine();

      System.out.print("Enter the amount: ");
      float amountFloat = input.nextFloat();
      BigDecimal amount = BigDecimal.valueOf(amountFloat);

      String priority = null;
      if ("expenses".equalsIgnoreCase(type)) {
        System.out.print("Enter the priority (Need/Want/Savings): ");
        priority = input.nextLine();

        if (!priority.equals("Need") && !priority.equals("Want") && !priority.equals("Savings")) {
          System.out.println("Invalid priority. It must be one of: Need, Want, Savings.");
          continue;
        }
      }

      BudgetSheet newField = new BudgetSheet(type, name, amount, priority, budgetSheetName);
      newField.save();

      System.out.print("Would you like to add another field (yes/no)? ");
      String response = input.nextLine();
      addingFields = response.equalsIgnoreCase("yes");
    }

    System.out.println("Budget sheet created successfully!");
  }

  public static void viewMyBudgetSheet() {
    System.out.print("Enter budget sheet name to view: ");

    String budgetSheetName = input.nextLine();

    ArrayList<BudgetSheet> sheets = BudgetSheet.find(budgetSheetName);

    if (sheets.isEmpty()) {
      System.out.println("No budget sheet found with the name: " + budgetSheetName);
    } else {
      BigDecimal totalIncome = BigDecimal.ZERO;
      BigDecimal totalExpense = BigDecimal.ZERO;

      System.out.println(budgetSheetName);

      System.out.println("Income:");
      for (BudgetSheet sheet : sheets) {
        if (sheet.type.equals("income")) {
          System.out.println("Name: " + sheet.title + ", Amount: " + sheet.amount);
          totalIncome = totalIncome.add(sheet.amount);
        }
      }

      System.out.println("Expenses:");
      for (BudgetSheet sheet : sheets) {
        if (sheet.type.equals("expenses")) {
          System.out.println("Name: " + sheet.title + ", Amount: " + sheet.amount + ", Priority: " + sheet.priority);
          totalExpense = totalExpense.add(sheet.amount);
        }
      }

      System.out.println("Total Income: " + totalIncome);
      System.out.println("Total Expenses: " + totalExpense);
      System.out.println("Net Balance: " + totalIncome.subtract(totalExpense));
    }
  }

  public static void editBudgetSheetFields() {
    System.out.print("Enter budget sheet name to edit: ");
    String sheetName = input.nextLine();

    ArrayList<BudgetSheet> sheets = BudgetSheet.find(sheetName);

    if (sheets.isEmpty()) {
      System.out.println("No budget sheets found.");
    } else {
      System.out.println("Select a field to edit:");

      for (int i = 0; i < sheets.size(); i++) {
        System.out.println((i + 1) + ". " + sheets.get(i).type + ": " + sheets.get(i).title);
      }

      System.out.print("Select a field by number: ");
      int fieldNumber = input.nextInt();
      input.nextLine();

      if (fieldNumber < 1 || fieldNumber > sheets.size()) {
        System.out.println("Invalid selection.");
        return;
      }

      BudgetSheet selectedSheet = sheets.get(fieldNumber - 1);

      System.out.println("You selected: " + selectedSheet.title);

      System.out.println("What would you like to edit?");
      System.out.println("1. Name (Currently: " + selectedSheet.title + ")");
      System.out.println("2. Amount (Currently: " + selectedSheet.amount + ")");
      if (selectedSheet.type.equals("expenses")) {
        System.out.println("3. Priority (Currently: " + selectedSheet.priority + ")");
      }

      System.out.print("Select an option to edit: ");
      int editOption = input.nextInt();
      input.nextLine();

      switch (editOption) {
        case 1:
          System.out.print("Enter new name: ");
          selectedSheet.title = input.nextLine();
          break;
        case 2:
          System.out.print("Enter new amount: ");
          float newAmount = input.nextFloat();
          selectedSheet.amount = BigDecimal.valueOf(newAmount);
          input.nextLine();
          break;
        case 3:
          if (selectedSheet.type.equals("expenses")) {
            System.out.print("Enter new priority (Need/Want/Savings): ");
            selectedSheet.priority = input.nextLine();
          } else {
            System.out.println("This field does not have a priority.");
          }
          break;
        default:
          System.out.println("Invalid option.");
          break;
      }

      System.out.println("Field updated successfully!");
    }
  }

  public static void help() {
    final String help = """
        | 1. Create A New Budget Sheet
        Upon selection, this option will ask the user
        to give a new name for their budget sheet. After that,
        an empty table will be displayed showing column headings
        'Type,' 'Title,' and 'Amount.' These are the two types of fields
        the user will be able to create, read, update, and delete.
        Income example:
        An example of an income could look like a weekly paycheck.
        The user could give that field a name of 'Paycheck', the amount ($300).
        An example of an expense could be 'Gas Money'.
        The user could give that field a name of 'Expenses', the cost ($45),
        and the priority of the Expense.
        The priority field ranges from 3 different types:
        1. Need, 2. Want, and 3. Savings.
        """;
    System.out.print(help);
  }
}
