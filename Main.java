import budget.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();

        BudgetManager manager = new BudgetManager(userName);

        int choice;

        do {
            System.out.println("\n=== Personal Budget Management System ===");
            System.out.println("1. Add Income Entry");
            System.out.println("2. Add Expense Entry");
            System.out.println("3. View Expense Summary");
            System.out.println("4. View Past Transactions");
            System.out.println("5. Generate Monthly Report");
            System.out.println("6. Set Savings Goal");
            System.out.println("7. Edit/Delete Entry");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    manager.addIncome();
                    break;
                case 2:
                    manager.addExpense();
                    break;
                case 3:
                    manager.viewExpenseSummary();
                    break;
                case 4:
                    manager.viewTransactions();
                    break;
                case 5:
                    manager.generateReportToFile();
                    break;
                case 6:
                    manager.setSavingsGoal();
                    break;
                case 7:
                    manager.editOrDeleteEntry();
                    break;
                case 8:
                    System.out.println("Exiting the system.");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 8);

        scanner.close();
    }
}
