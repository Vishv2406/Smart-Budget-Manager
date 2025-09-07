package budget;

public class Expense extends Transaction {
    public Expense(String description, double amount, String date) {
        super(description, amount, date);
    }

    public void setCategory(String category) {
        setDescription(category);
    }
}
