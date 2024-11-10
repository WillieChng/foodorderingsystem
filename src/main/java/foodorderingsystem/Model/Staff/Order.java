package foodorderingsystem.Model.Staff;

public class Order {
    private int tableNumber;
    private String name;
    private int quantity;
    private double price; // Add a price field

    public Order(int tableNumber, String name, int quantity, double price) {
        this.tableNumber = tableNumber;
        this.name = name;
        this.quantity = quantity;
        this.price = price; // Initialize the price field
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s x %d     ----------     RM%.2f", name, quantity, price);
    }
}