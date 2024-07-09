package au.edu.sydney.soft3202.task1;

import java.util.Locale;

public class Item {
    String name;
    Double cost;
    Integer qty;

    public Item(String name, Double cost, Integer qty) {
        this.name = name.toLowerCase();
        this.cost = cost;
        this.qty = qty;
    }

    public String getName() {
        return name.toLowerCase();
    }

    public void setName(String name) {
        if (name.isEmpty() || name == null) {
            throw new IllegalArgumentException("name may not be empty or null");
        } else {
            this.name = name.toLowerCase();;
        }
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) throws IllegalArgumentException{
        if (cost <= 0 || cost > Double.MAX_VALUE || cost == null) {
            throw new IllegalArgumentException("cost cannot be > MAX_VALUE or <= 0 or null");
        } else {
            this.cost = cost;
        }
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(int newQty) {
        if (newQty < 0 ) {
            throw new IllegalArgumentException("qty: " + newQty +  "cannot be > MAX_VALUE or < 0 or null");
        }
        this.qty = newQty;

    }
}
