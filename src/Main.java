import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
          3. Create Budget Sheet Fields
          4. Edit Budget Sheet Fields
          5. Exit
        
          """;
      System.out.print(userOptions);
      System.out.print("> ");
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
          createBudgetSheetFields();
          break;
        case 4:
          editBudgetSheetFields();
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
    String budgetSheetName = input.nextLine();

    for (BudgetSheet sheet : allBudgetSheets) {
      if (sheet.name.equalsIgnoreCase(budgetSheetName)) {
        System.out.println("Budget sheet '" + budgetSheetName + "' has already been created.");
        return;
      }
    }

    BudgetSheet newSheet = new BudgetSheet(budgetSheetName);
    newSheet.save();
    allBudgetSheets.add(newSheet);

    System.out.println("Would you like to add income/expense fields to the budget sheet? (y/n)");
    String answer = input.nextLine();

    if(answer.equalsIgnoreCase("y")) {
      boolean addingFields = true;
      while (addingFields) {
        System.out.print("Enter the type (income/expenses): ");
        String type = input.nextLine();



        System.out.print("Enter the name of the field: ");
        String name = input.nextLine();

        System.out.print("Enter the amount: ");
        float amountFloat = input.nextFloat();
        BigDecimal amount = BigDecimal.valueOf(amountFloat).setScale(2, RoundingMode.HALF_UP);;
        input.nextLine();

        String priority = null;
        if ("expenses".equalsIgnoreCase(type)) {
          System.out.print("Enter the priority (Need/Want/Savings): ");
          priority = input.nextLine();

          if (!priority.equals("Need") && !priority.equals("Want") && !priority.equals("Savings")) {
            System.out.println("Invalid priority. It must be one of: Need, Want, Savings.");
            continue;
          }
        }

        Transaction newTransaction = new Transaction(type, name, amount, priority, newSheet.id);
        newTransaction.save();


        newSheet.transactions.add(newTransaction);

        System.out.print("Would you like to add another field (yes/no)? ");
        String response = input.nextLine();
        addingFields = response.equalsIgnoreCase("yes");
      }

    } else if ("n".equalsIgnoreCase(answer)) {
      System.out.println("Budget sheet created successfully");
      return;
    }





    System.out.println("Budget sheet created successfully!");
  }


  public static void createBudgetSheetFields() {
    System.out.print("Which budget sheet would you like to add fields to? ");
    String budgetSheetName = input.nextLine();


    ArrayList<BudgetSheet> sheets = BudgetSheet.find(budgetSheetName);


    if (sheets.isEmpty()) {
      System.out.println("No budget sheet found with the name: " + budgetSheetName);
      return;
    }


    BudgetSheet sheet = sheets.get(0);

    boolean addingFields = true;
    while (addingFields) {
      System.out.print("Enter the type of field (income/expenses): ");
      String type = input.nextLine();


      if (!type.equalsIgnoreCase("income") && !type.equalsIgnoreCase("expenses")) {
        System.out.println("Invalid type. Please enter 'income' or 'expenses'.");
        continue;
      }

      System.out.print("Enter the name for the field: ");
      String name = input.nextLine();

      System.out.print("Enter the amount: ");
      float amountFloat = input.nextFloat();
      BigDecimal amount = BigDecimal.valueOf(amountFloat);
      input.nextLine();

      String priority = null;
      if ("expenses".equalsIgnoreCase(type)) {
        System.out.print("Enter the priority (Need/Want/Savings): ");
        priority = input.nextLine();

        if (!priority.equals("Need") && !priority.equals("Want") && !priority.equals("Savings")) {
          System.out.println("Invalid priority. It must be one of: Need, Want, Savings.");
          continue;
        }
      }


      Transaction newTransaction = new Transaction(type, name, amount, priority, sheet.id);
      newTransaction.save();

      System.out.print("Would you like to add another field (yes/no)? ");
      String response = input.nextLine();
      addingFields = response.equalsIgnoreCase("yes");
    }

    System.out.println("Fields added successfully to the budget sheet: " + sheet.name);
  }


  public static void viewMyBudgetSheet() {

    if (allBudgetSheets.isEmpty()) {
      System.out.println("No budget sheets available.");
      return;
    }


    System.out.println("Here are your budget sheets:");
    for (int i = 0; i < allBudgetSheets.size(); i++) {
      System.out.println((i + 1) + ". " + allBudgetSheets.get(i).name);
    }


    System.out.print("Select a budget sheet to view by number: ");
    int selectedSheetIndex = input.nextInt();
    input.nextLine();


    if (selectedSheetIndex < 1 || selectedSheetIndex > allBudgetSheets.size()) {
      System.out.println("Invalid selection. Please choose a valid number.");
      return;
    }


    BudgetSheet selectedSheet = allBudgetSheets.get(selectedSheetIndex - 1);


    BigDecimal totalIncome = BigDecimal.ZERO;
    BigDecimal totalExpense = BigDecimal.ZERO;


    System.out.println("Budget Sheet: " + selectedSheet.name);


    System.out.println("Income:");

    boolean incomeFound = false;
    for (Transaction txn : selectedSheet.transactions) {
      if (txn.type.equals("income")) {
        System.out.println("Name: " + txn.title + ", Amount: " + txn.amount);
        totalIncome = totalIncome.add(txn.amount.setScale(2, RoundingMode.HALF_UP));

        incomeFound = true;
      }



    }
    if (!incomeFound) {
      System.out.println("No income fields found.");
    }
    boolean expenseFound = false;
    System.out.println("Expenses:");
    for (Transaction txn : selectedSheet.transactions) {
      if (txn.type.equals("expenses")) {
        System.out.println("Name: " + txn.title + ", Amount: " + txn.amount + ", Priority: " + txn.priority);
        totalExpense = totalExpense.add(txn.amount.setScale(2, RoundingMode.HALF_UP));

        expenseFound = true;
      } else {
        System.out.println("No expense fields found.");
      }
    }
    if (!expenseFound) {
      System.out.println("No expense fields found.");
    }
    System.out.println("Total Income: " + totalIncome);
    System.out.println("Total Expenses: " + totalExpense);
    System.out.println("Net Balance: " + totalIncome.subtract(totalExpense));

    System.out.println("Would you like to view your budget goals? (y/n)?");
    String response = input.nextLine();
    if (response.equalsIgnoreCase("y")) {
      viewMyBudgetGoals();
    } else {
      return;
    }
  }




  public static void editBudgetSheetFields() {
    if (allBudgetSheets.isEmpty()) {
      System.out.println("No budget sheets available.");
      return;
    }


    System.out.println("Here are your budget sheets:");
    for (int i = 0; i < allBudgetSheets.size(); i++) {
      System.out.println((i + 1) + ". " + allBudgetSheets.get(i).name);
    }


    System.out.print("Select the budget sheet you would like to edit by number: ");
    int selectedSheetIndex = input.nextInt();
    input.nextLine();

    if (selectedSheetIndex < 1 || selectedSheetIndex > allBudgetSheets.size()) {
      System.out.println("Invalid selection. Please choose a valid number.");
      return;
    }

    BudgetSheet sheet = allBudgetSheets.get(selectedSheetIndex - 1); // Get the selected sheet

    boolean doneEditing = false;
    while (!doneEditing) {
      System.out.println("Here are your options for editing " + sheet.name + ":");
      String userOptions = """
            1. Change The Name of A Budget Sheet
            2. Edit An Expense Field
            3. Edit An Income Field
            4. Delete A Budget Sheet
            5. Return to Main Menu
            """;

      System.out.print(">");
      System.out.print(userOptions);

      int option = input.nextInt();
      input.nextLine();

      switch (option) {
        case 1:

          System.out.print("Enter the new name for the budget sheet: ");
          String newName = input.nextLine();
          sheet.name = newName;
          sheet.save();
          System.out.println("Budget sheet name updated successfully.");
          break;

        case 2:
          editExpenseField(sheet);

          break;

        case 3:
          editIncomeField(sheet);

          break;

        case 4:

          System.out.print("Are you sure you want to delete this budget sheet? (yes/no): ");
          String confirmDelete = input.nextLine();

          if (confirmDelete.equalsIgnoreCase("yes")) {

            sheet.delete();


            allBudgetSheets.remove(sheet);

            System.out.println("Budget sheet deleted successfully.");
            doneEditing = true;
          } else {
            System.out.println("Deletion cancelled.");
          }
          break;

        case 5:
          doneEditing = true;
          break;

        default:
          System.out.println("Invalid option. Please select a valid number.");
          break;
      }
    }
  }

  public static void editExpenseField(BudgetSheet sheet) {
    System.out.println("Here are your expense fields:");

    ArrayList<Transaction> expenseTransactions = new ArrayList<>();
    for (Transaction txn : sheet.transactions) {
      if (txn.type.equals("expenses")) {
        expenseTransactions.add(txn);
        System.out.println(expenseTransactions.size() + ". " + txn.title + ", Amount: " + txn.amount + ", Priority: " + txn.priority);
      }
    }

    if (expenseTransactions.isEmpty()) {
      System.out.println("No expenses found.");
      return;
    }

    System.out.print("Select an expense field to edit by number: ");
    int selectedExpenseIndex = input.nextInt();
    input.nextLine();

    if (selectedExpenseIndex < 1 || selectedExpenseIndex > expenseTransactions.size()) {
      System.out.println("Invalid selection. Please choose a valid number.");
      return;
    }

    Transaction selectedExpense = expenseTransactions.get(selectedExpenseIndex - 1);

    System.out.println("Editing expense: " + selectedExpense.title);
    System.out.print("Enter new name for the expense: ");
    String newName = input.nextLine();
    selectedExpense.title = newName;

    System.out.print("Enter new amount for the expense: ");
    float newAmountFloat = input.nextFloat();
    selectedExpense.amount = BigDecimal.valueOf(newAmountFloat);
    input.nextLine();

    System.out.print("Enter new priority for the expense (Need/Want/Savings): ");
    String newPriority = input.nextLine();
    if (newPriority.equals("Need") || newPriority.equals("Want") || newPriority.equals("Savings")) {
      selectedExpense.priority = newPriority;
    } else {
      System.out.println("Please enter 1-3 priority types");
    }

    selectedExpense.save();
    System.out.println("Expense field updated.");
  }

  public static void editIncomeField(BudgetSheet sheet) {
    System.out.println("Here are your income fields:");

    ArrayList<Transaction> incomeTransactions = new ArrayList<>();
    for (Transaction txn : sheet.transactions) {
      if (txn.type.equals("income")) {
        incomeTransactions.add(txn);
        System.out.println(incomeTransactions.size() + ". " + txn.title + ", Amount: " + txn.amount);
      }
    }

    if (incomeTransactions.isEmpty()) {
      System.out.println("No income found.");
      return;
    }

    System.out.print("Select an income field to edit by number: ");
    int selectedIncomeIndex = input.nextInt();
    input.nextLine();

    if (selectedIncomeIndex < 1 || selectedIncomeIndex > incomeTransactions.size()) {
      System.out.println("Invalid selection. Please choose a valid number.");
      return;
    }

    Transaction selectedIncome = incomeTransactions.get(selectedIncomeIndex - 1);

    System.out.println("Editing income: " + selectedIncome.title);
    System.out.print("Enter new name for the income: ");
    String newName = input.nextLine();
    selectedIncome.title = newName;

    System.out.print("Enter new amount for the income: ");
    float newAmountFloat = input.nextFloat();
    selectedIncome.amount = BigDecimal.valueOf(newAmountFloat);
    input.nextLine();

    selectedIncome.save();
    System.out.println("Income updated successfully.");
  }

  public static void viewMyBudgetGoals() {


    if (allBudgetSheets.isEmpty()) {
      System.out.println("No budget sheets available.");
      return;
    }

    System.out.print("Which budget sheet would you like to view your goals for? ");
    String budgetSheetName = input.nextLine();

    ArrayList<BudgetSheet> sheets = BudgetSheet.find(budgetSheetName);

    if (sheets.isEmpty()) {
      System.out.println("No budget sheet found with the name: " + budgetSheetName);
      return;
    }
    System.out.println("Here are your budget goals for" + budgetSheetName + ":");
    BudgetSheet selectedSheet = sheets.get(0);


    BigDecimal totalIncome = BigDecimal.ZERO;
    for (Transaction txn : selectedSheet.transactions) {
      if (txn.type.equals("income")) {
        totalIncome = totalIncome.add(txn.amount);
      }
    }


    BigDecimal targetNeeds = totalIncome.multiply(BigDecimal.valueOf(0.50));
    BigDecimal targetWants = totalIncome.multiply(BigDecimal.valueOf(0.30));
    BigDecimal targetSavings = totalIncome.multiply(BigDecimal.valueOf(0.20));


    BigDecimal actualNeeds = BigDecimal.ZERO;
    BigDecimal actualWants = BigDecimal.ZERO;
    BigDecimal actualSavings = BigDecimal.ZERO;


    for (Transaction txn : selectedSheet.transactions) {
      if (txn.type.equals("expenses")) {
        if ("Need".equalsIgnoreCase(txn.priority)) {
          actualNeeds = actualNeeds.add(txn.amount);
        } else if ("Want".equalsIgnoreCase(txn.priority)) {
          actualWants = actualWants.add(txn.amount);
        } else if ("Savings".equalsIgnoreCase(txn.priority)) {
          actualSavings = actualSavings.add(txn.amount);
        }
      }
    }


    BigDecimal percentNeeds = (totalIncome.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO :
            actualNeeds.multiply(BigDecimal.valueOf(100)).divide(totalIncome, 2, RoundingMode.HALF_UP);
    if (percentNeeds.compareTo(BigDecimal.valueOf(100)) > 0) {
      percentNeeds = BigDecimal.valueOf(100);
    }

    BigDecimal percentWants = (totalIncome.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO :
            actualWants.multiply(BigDecimal.valueOf(100)).divide(totalIncome, 2, RoundingMode.HALF_UP);
    if (percentWants.compareTo(BigDecimal.valueOf(100)) > 0) {
      percentWants = BigDecimal.valueOf(100);
    }

    BigDecimal percentSavings = (totalIncome.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO :
            actualSavings.multiply(BigDecimal.valueOf(100)).divide(totalIncome, 2, RoundingMode.HALF_UP);
    if (percentSavings.compareTo(BigDecimal.valueOf(100)) > 0) {
      percentSavings = BigDecimal.valueOf(100);
    }



    System.out.println("Income: " + "$" + totalIncome);
    System.out.println("Goal For Needs (50%): " + "$" + targetNeeds + ", Actual Needs: " + "$" + actualNeeds + " (" + percentNeeds + "%)");
    System.out.println("Goal For Wants (30%): " + "$" + targetWants + ", Actual Wants: " + "$" + actualWants + " (" + percentWants + "%)");
    System.out.println("Goal For Savings (20%): " + "$" + targetSavings + ", Actual Savings: " + "$" + actualSavings + " (" + percentSavings + "%)");


    BigDecimal balance = totalIncome.subtract(actualNeeds).subtract(actualWants).subtract(actualSavings);
    System.out.println("Balance after goals: " + balance);
  }

}
