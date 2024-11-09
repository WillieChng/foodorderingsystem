package foodorderingsystem.Model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<MenuItem, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addItem(MenuItem item) {
        items.put(item, items.getOrDefault(item, 0) + 1);
    }

    public void removeItem(MenuItem item) {
        if (items.containsKey(item)) {
            int quantity = items.get(item);
            if (quantity > 1) {
                items.put(item, quantity - 1);
            } else {
                items.remove(item);
            }
        }
    }

    public Map<MenuItem, Integer> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}