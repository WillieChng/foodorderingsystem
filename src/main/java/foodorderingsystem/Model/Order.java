package foodorderingsystem.Model;

public class Order {
    private int tableNumber;
    private String details;

    public Order(int tableNumber, String details) {
        this.tableNumber = tableNumber;
        this.details = details;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}