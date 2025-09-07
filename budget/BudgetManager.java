package budget;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BudgetManager {
    private double income;
    private double expenses;
    private List<Transaction> transactions = new ArrayList<>(); // ArrayList
    private double savingsGoal;
    private final double BUDGET_LIMIT = 500000.0;
    private String userName;
    
    // Constructor
    public BudgetManager(String userName) {
        this.income = 0.0;
        this.expenses = 0.0;
        this.savingsGoal = 0.0;
        this.userName = userName;
    }

    public void addIncome() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter income category: ");
        String category = scanner.nextLine();
        System.out.print("Enter amount of income: ");
        double income = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter date (dd/mm/yyyy): ");
        String date = validateDate(scanner);

        this.income += income;
        transactions.add(new Income(category, income, date));
        System.out.println("Income added successfully.");
    }

    public void addExpense() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expense category: ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter date (dd/mm/yyyy): ");
        String date = validateDate(scanner);

        this.expenses += amount;
        transactions.add(new Expense(category, amount, date));

        if (this.expenses > BUDGET_LIMIT) {
            System.out.println("Warning: You've exceeded your budget limit!");
        }

        System.out.println("Expense added successfully.");
    }

    public void generateReportToFile() {
        transactions.sort(Comparator.comparing(Transaction::getDate));

        double totalIncome = 0.0;
        double totalExpenses = 0.0;
        double netSavings = 0.0;

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("=== Budget Report ===\n");
        reportContent.append("Name: ").append(userName).append("\n");
        reportContent.append("---------------------------------------------------------\n");
        reportContent.append(String.format("%-12s %-15s %-10s %-10s %-10s\n", "Date", "Category", "Income (₹)", "Expense (₹)", "Savings (₹)"));
        reportContent.append("---------------------------------------------------------\n");

        for (Transaction transaction : transactions) {
            double incomeAmount = 0.0;
            double expenseAmount = 0.0;

            if (transaction instanceof Income) {
                incomeAmount = transaction.getAmount();
                totalIncome += incomeAmount;
            } else if (transaction instanceof Expense) {
                expenseAmount = transaction.getAmount();
                totalExpenses += expenseAmount;
            }

            netSavings = totalIncome - totalExpenses;
            reportContent.append(String.format("%-12s %-15s %-10s %-10s %-10s\n",
                    transaction.getDate(),
                    transaction.getDescription(),
                    formatCurrency(incomeAmount),
                    formatCurrency(expenseAmount),
                    formatCurrency(netSavings)));
        }

        
        netSavings = totalIncome - totalExpenses;

       
        reportContent.append("---------------------------------------------------------\n");
        reportContent.append(String.format("Total Income: %s\n", formatCurrency(totalIncome)));
        reportContent.append(String.format("Total Expenses: %s\n", formatCurrency(totalExpenses)));
        reportContent.append(String.format("Net Savings: %s\n", formatCurrency(netSavings)));
        reportContent.append(String.format("Savings Goal: %s\n", formatCurrency(savingsGoal)));
        reportContent.append("---------------------------------------------------------\n");

       
        try {
            FileWriter fileWriter = new FileWriter("Budget_Report.txt");
            fileWriter.write(reportContent.toString());
            fileWriter.close();
            System.out.println("Report saved as 'Budget_Report.txt'.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing the report to a file.");
            e.printStackTrace();
        }
    }


    private String formatCurrency(double amount) {
        return "₹" + String.format("%.2f", amount);
    }

    private String validateDate(Scanner scanner) {
        String date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        while (true) {
            date = scanner.nextLine();
            try {
                dateFormat.parse(date);
                break; 
            } catch (ParseException e) {
                System.out.print("Invalid date format. Please enter again (dd/mm/yyyy): ");
            }
        }
        return date;
    }

    public void viewExpenseSummary() {
        System.out.println("\n--- Expense Summary ---");
        System.out.println("Total Income: " + formatCurrency(this.income));
        System.out.println("Total Expenses: " + formatCurrency(this.expenses));
        System.out.println("Net Savings: " + formatCurrency(this.income - this.expenses));
    }

    public void viewTransactions() {
        System.out.println("\n=== Past Transactions ===");
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println((i + 1) + ". " + transactions.get(i));
        }
    }

    public void editOrDeleteEntry() {
        Scanner scanner = new Scanner(System.in);
        viewTransactions();
        System.out.print("\nSelect the transaction number to edit/delete (or 0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= transactions.size()) {
            Transaction selected = transactions.get(choice - 1);
            System.out.println("Selected: " + selected);
            System.out.println("1. Edit");
            System.out.println("2. Delete");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                System.out.print("Enter new date (dd/mm/yyyy): ");
                String newDate = validateDate(scanner);
                selected.setDate(newDate);

                if (selected instanceof Expense) {
                    expenses -= selected.getAmount(); 
                    System.out.print("Enter new expense category: ");
                    String newCategory = scanner.nextLine();
                    System.out.print("Enter new amount: ");
                    double newAmount = scanner.nextDouble();
                    scanner.nextLine();
                    ((Expense) selected).setCategory(newCategory);
                    selected.setAmount(newAmount);
                    expenses += newAmount;
                } else {
                    income -= selected.getAmount();
                    System.out.print("Enter new income category: ");
                    String newCategory = scanner.nextLine();
                    System.out.print("Enter new income amount: ");
                    double newAmount = scanner.nextDouble();
                    scanner.nextLine(); 
                    selected.setAmount(newAmount);
                    selected.setDescription(newCategory);
                    income += newAmount; 
                }
                System.out.println("Transaction updated successfully.");
            } else if (option == 2) {
                transactions.remove(choice - 1);
                System.out.println("Transaction deleted successfully.");
            } else {
                System.out.println("Invalid option selected.");
            }
        } else {
            System.out.println("No valid transaction selected.");
        }
    }

    public void setSavingsGoal() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter savings goal: ");
        this.savingsGoal = scanner.nextDouble();
        System.out.println("Savings goal set to: " + this.savingsGoal);
    }
}
