import java.util.*;

public class Main {
  final static Scanner input = new Scanner(System.in);

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

      int option = input.nextInt();

      switch (option) {
        case 1:
          System.out.print("Calling budget sheet method");
          createNewBudgetSheet();
          break;
        case 2:
          System.out.print("View budget sheet method");
          viewMyBudgetSheet();
          break;
        case 3:
          System.out.print("Edit budget sheet method");
          editBudgetSheetFields();
          break;
        case 4:
          System.out.print("Help method");
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
    System.out.print("Enter budget sheet name: ");
  }

  public static void viewMyBudgetSheet() {
    System.out.print("Enter budget sheet name: ");
  }

  public static void editBudgetSheetFields() {
    System.out.print("Enter budget sheet name: ");
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

        The user can select 'Go back to menu' if they are done adding these fields.
        ______________________________
        | 2. View My Budget Sheets:
        After creating a budget sheet, all the information in that sheet is stored in a list
        for viewing. That could look something like this:
        Sheet Names:
        1. January Expenses
        2. Car Expenses
        3. Insurance
        Selecting 1. would pull up all data for January Expenses, including the income and expense fields.
        Aside from simply being able to view the stored data, the user should also be able to filter that data too.
        If the user want to find all fields with the priority of 'Need,' it should display the data for that query.
        This is just one of the examples of how the user should be able to filter data.
        ______________________________
        | 3. Edit Budget Sheet Fields
        Upon selection, this option will allow the user to edit specific data inside their
        budget sheet table. This could look something like this:
        Select a table to edit:
        1. January Expenses
        2. Car Expenses
        3. Insurance
        User: "1"
        Which type of data would you like to edit?
        1. Table Name (Currently: Insurance)
        2. Income Fields
        3. Expense Fields
        User: "1"
        What would you like to change the name to?
        User: "Car Note Payment"
        Which type of data would you like to edit?
        User "2"
        Which income field would you like to edit?
        User: "Weekly Paycheck"
        Here's what you can edit:
        1. Field name (Currently: Weekly Paycheck)
        2. Field amount (Currently: $350)

        Which type of data would you like to edit?
        1. Table Name (Currently: Car Note Payment)
        2. Income Fields
        3. Expense Fields

        User: "3"

        Which expense field would you like to edit?
        1. Ford F-150
        2. Nissan Frontier

        User: "1"
        1. Field name (Currently: Ford F-150)
        2. Field amount (Currently: $1200)
        3. Priority (Currently: Need)
        """;
    System.out.print(help);
  }
}
