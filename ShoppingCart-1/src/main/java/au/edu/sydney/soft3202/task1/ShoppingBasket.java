package au.edu.sydney.soft3202.task1;

import java.util.ArrayList;
import java.util.Locale;

/**
* Container for items to be purchased
*/
public class ShoppingBasket {

    ArrayList<Item> cart;
    ArrayList<String> names;
    String[] defaultCartItems = {"apple", "orange", "pear", "banana"};

    /**
    * Creates a new, empty ShoppingBasket object
    */
    public ShoppingBasket() {

        names = new ArrayList<>();
        cart = new ArrayList<>();
        cart.add(new Item("apple", 2.5, 0));
        names.add("apple");
        cart.add(new Item("orange", 1.25, 0));
        names.add("orange");
        cart.add(new Item("pear", 3.0, 0));
        names.add("pear");
        cart.add(new Item("banana", 4.95, 0));
        names.add("banana");

    }

    public ArrayList<Item> getCart() {
        return cart;
    }

    public Double getItemCost(String name) {
        Double cost = 0.0;
        for (Item item : cart) {
            if (item.getName().equals(name.toLowerCase())) {
                cost = item.getCost();
            }
        }
        return cost;
    }


    public void addItem(String name, int qty) {
        if (name == null || !names.contains(name.toLowerCase())) {
            throw new IllegalArgumentException("item name must exist in cart and cannot be null");
        }
        if (qty <= 0 || qty > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Qty to be added must not be <= 0 or > MAX_VALUE");
        }
        for (Item item : cart) {
            if (item.getName().contains(name.toLowerCase())) {
                item.setQty(item.getQty() + qty);
            }
        }
    }

    public Boolean addNewItem(String name, Double cost) throws IllegalArgumentException {
        if (cost == null) {
            return false;
        }
        if (name == null || cost < 0 || cost > Double.MAX_VALUE || name.isEmpty() || cost == null) {
            return false;
        }
        for (Item item : cart) {
            if (name.toLowerCase().equals(item.getName().toLowerCase())) {
                return false;
            }
        }
        names.add(name.toLowerCase());
        this.cart.add(new Item(name, cost, 0));

        return true;
    }

    public ArrayList<Item> updateItemQty(String name, int qty) {
        for (Item item : cart) {
            if (item.getName().equals(name.toLowerCase())) {
                item.setQty(qty);
            }
        }
        return cart;
    }

    public int getQty(String name) {
        for (Item item : cart ) {
            if (item.getName().equals(name.toLowerCase())) {
                return item.getQty();
            }
        }
        return 0;
    }


    //remove qty from specified item (name)
    public boolean removeItem(String name, int newQty) throws IllegalArgumentException{
        if (name == null || !names.contains(name.toLowerCase())) {
            throw new IllegalArgumentException("name cannot be null and must be in existing names list!");
        } if (newQty > Integer.MAX_VALUE || newQty <= 0) {
            throw new IllegalArgumentException("Qty to remove cannot be less than zero or greater than MAX_INT or greater than current item qty ");
        }
        for (Item item : cart) {
            if (item.getName().equals(name.toLowerCase())) {
                int finalQty = item.getQty() - newQty;
                if (finalQty < 0)  {
                    return false;
                } else {
                    item.setQty(finalQty);
                    return true;
                }
            }
        }
        return false;
    }

    //Delete item completely from cart
    public void deleteItem(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        for(int i = 0; i < cart.size(); i++) {
            if(cart.get(i).getName().equals(name.toLowerCase())) {
                cart.remove(i);
            }
        }
    }

//WAS USING THIS AS A PATCH FOR JMH TEST BUG
//    public boolean isQtyZero(String itemName) {
//        for (Item item : cart) {
//            if (item.getQty() == 0) {
//                    return true;
//            }
//        }
//        return false;
//    }

    //check if item is in cart, this makes sure deleteItem cannot be called for nonexisting item in controller
    public boolean cartContainsItem(String itemName) {
        for (Item item : cart) {
//            if (item.name.equals(itemName.toLowerCase()))
            if (item.getName().equals(itemName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }



    public Double getCartTotal() {
        Double total = 0.0;
        for (Item item : cart) {

            total += item.getCost() * item.getQty();
        }
        return total;
    }

    public void setNewCart(ArrayList<Item> newCart) {
        this.cart.clear();
        this.cart.addAll(newCart);
        this.names.clear();
        for(Item item : cart) {
            names.add(item.getName());
        }
    }

    public void clearCart() {
       this.cart.clear();
       this.names.clear();
    }

}
