package au.edu.sydney.soft3202.task1;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.DoubleArbitrary;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingBasketPropertyTest {

    ShoppingBasket sb = new ShoppingBasket();

    //NOTE THAT MY Sb IMPLEMENTATION RETURNS 0 FOR EMPTY CART RATHER THAN NULL.
    //this just tests that a new shopping basket total cost starts at 0
    @Property
    void getValueEmptyPropertyTest() {
        ShoppingBasket sb = new ShoppingBasket();
        //update null to 0?
        assertThat(sb.getCartTotal()).isEqualTo(0);
    }

    //provider for qty to use in addItem()
    @Provide
    Arbitrary<Integer> itemQty() {
        return Arbitraries.integers().between(1,Integer.MAX_VALUE);
    }

    //provider for name to use in addItem() provides a name from the names list in shopping basket
    // this only contains "apple" "pear" "orange" "banana" when a new shoppingBasket is created.
    // Unless a new item is added through sb.addNewItem(), then the new item name will be added to names list.
    @Provide
    Arbitrary<String> name() {
        return Arbitraries.of(sb.names);
    }


    //If n number of any specific items are added, the cost should reflect that.
    //I assuming that this means that adding any qty (n, -> reflected in my itemQty provider)
    // for any specific items (any specific reffering to any item in sb.names list --> reflected in my name provider)
    //should be reflected in cost
    // (ie 1 x apple@$1, 2 x pear@$1, 3 x orange@$1, 4 x banana@$1 --> Total cost = 1*1 + 2*1 + 3*1 + 4*1 = 10)
    //This is tested here:
    @Property
    void addNItemsEqualsCost(@ForAll("itemQty") int qty, @ForAll("name") String item) {
        ShoppingBasket sb = new ShoppingBasket();
        sb.addItem(item, qty);
        Double itemCost = sb.getItemCost(item);
        assertThat(sb.getCartTotal()).isEqualTo(itemCost * qty);
    }

    //this is the other way I interpereted the above test condition (n is a set number)
    @Property
    void addNItemsEqualsCost2(@ForAll("name") String item) {
        ShoppingBasket sb = new ShoppingBasket();
        int n = 5;
        sb.addItem(item, n);
        Double itemCost = sb.getItemCost(item);
        Double finalCost = itemCost * n;
        assertThat(sb.getCartTotal()).isEqualTo(finalCost);
    }

    //I am assuming n can be any arbitary number between 1 and max_value
    //tests that if n items of any kind from names list is added and then removed the cart total will be 0.
    //note that original code scafold returned null for 0 cost but i changed it to return 0
    @Property
    void zeroCostAfterAddingNItemsAndRemovingNItems(@ForAll("itemQty") int qty, @ForAll("name") String item) {
        ShoppingBasket sb = new ShoppingBasket();
        sb.addItem(item, qty);
        sb.removeItem(item, qty);
        assertThat(sb.getCartTotal()).isEqualTo(0);
    }

    //this is the other way I interpereted the above test condition (n is a set number)
    @Property
    void zeroCostAfterAddingNItemsAndRemovingNItems2(@ForAll("name") String item) {
        ShoppingBasket sb = new ShoppingBasket();
        int n = 10;
        sb.addItem(item, n);
        sb.removeItem(item, n);
        assertThat(sb.getCartTotal()).isEqualTo(0);
    }

    //provides arbitrary item cost for addNewItem() , used in my bonus mark test
    @Provide
    Arbitrary<Double> itemCost() {
        return Arbitraries.doubles().between(0.01,10.0);
    }

    //provides arbitray name for addNewItem(), used in my bonus mark test
    //min length = 1 becuase empty string cannot be accepted
    //set max length because the length of the string doesnt really matter but i wanted small outputs when i was printing added items
    @Provide
    Arbitrary<String> stringArbitrary() {
        return Arbitraries.strings().ofMinLength(1).ofMaxLength(100);
    }

    //
    @Provide
    Arbitrary<Integer> itemQty2() {
        return Arbitraries.integers().between(1,Integer.MAX_VALUE);
    }


    //Note: i cannot test for differnt user carts becuase my user cart details are stored in the controller.
    //My first bonus test attempt. I have made two assumptions on what the description of the requirements mean.
    // this is in regards to:
    // "arbitrary number of items can be added and removed so long as the count is with in 0 and INT_MAX."

    // Assumtion 1. My shopping basket class has a method updateQty() that can directly set the qty of an item to the
                //  provided qty in the parameter. I am assuming this satisfies "adding" and "removing" items.
                //SEE bounsTest1

    // Assumtion 2. Just incase the previous assumtion is incorect, I have a seccond version of the bonus attempt below
                //  The first that uses addItem() and removeItem() methods instead of my updateQty() method
                // it also uses two arbitary qty's so different qty's can be added and removed for the same arbitray item
                // SEE bonusTest2
    @Property
    void bonusTest1(@ForAll("itemQty2") int arbitraryQty, @ForAll("stringArbitrary") String arbitaryName, @ForAll("itemCost") Double arbitraryCost) {
        ShoppingBasket sb = new ShoppingBasket();
//        for (String item : sb.names) {
//            if (item.equals(arbitaryName)) return;
//        }

        //new arbitrary items can be added to cart and is tested here
        sb.addNewItem(arbitaryName, arbitraryCost);
        assertThat(sb.cartContainsItem(arbitaryName)).isTrue();

        //new arbitrary items names can update qty to arbitray qty ( MAX_VALUE >= qty >= 0) this is shown here
        //update is able to remove or add items by directly updating qty that is provided in parameter
        // this test satisfys both parts of the adding/removing arbitarty qty in the bonus test
        sb.updateItemQty(arbitaryName, arbitraryQty);
        assertThat(sb.getQty(arbitaryName)).isEqualTo(arbitraryQty);

        //new arbitrary items can be deleted from cart and is tested here
        sb.deleteItem(arbitaryName);
        assertThat(sb.cartContainsItem(arbitaryName)).isFalse();

    }

    @Property
    void bonusTest2(@ForAll("itemQty") int arbitraryQty, @ForAll("itemQty2") int arbitraryQty2, @ForAll("stringArbitrary") String arbitaryName, @ForAll("itemCost") Double arbitraryCost) {
        ShoppingBasket sb = new ShoppingBasket();
//        for (String item : sb.names) {
//            if (item.equals(arbitaryName)) return;
//        }

        //new arbitrary items can be added to cart and is tested here
        sb.addNewItem(arbitaryName, arbitraryCost);
        assertThat(sb.cartContainsItem(arbitaryName)).isTrue();

        //new arbitrary items names can add arbitray qty ( MAX_VALUE =< qty >= 1)
        // so long as the item count does not exceed Max_value or go below 0 addItem() will succeed.
        sb.addItem(arbitaryName, arbitraryQty);
        assertThat(sb.getQty(arbitaryName)).isEqualTo(arbitraryQty);

        //new arbitrary items names can remove arbitray qty ( MAX_VALUE =< qty >= 1)
        // so long as the item count does not exceed Max_value or go below 0 removeItem() will succeed.
        int newQty = arbitraryQty - arbitraryQty2;
        sb.removeItem(arbitaryName, arbitraryQty2);

        //qty will not be removed if it goes below 0 the below if statment uses the correct assertion based on this
        if (newQty < 0) {
            assertThat(sb.getQty(arbitaryName)).isEqualTo(arbitraryQty);
        } else {
            assertThat(sb.getQty(arbitaryName)).isEqualTo(newQty);
        }

        //new arbitrary items can be deleted from cart and is tested here
        sb.deleteItem(arbitaryName);
        assertThat(sb.cartContainsItem(arbitaryName)).isFalse();

    }

}
