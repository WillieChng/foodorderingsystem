package foodorderingsystem.Model.Staff;

public class Order {
    private int tableNumber, quantity;
    private String name;

    public Order(int tableNumber, String name, int quantity) {
        this.tableNumber = tableNumber;
        this.name = name;
        this.quantity = quantity;
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
}