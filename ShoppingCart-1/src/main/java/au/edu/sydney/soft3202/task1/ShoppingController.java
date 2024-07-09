package au.edu.sydney.soft3202.task1;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ShoppingController {
    private final SecureRandom randomNumberGenerator = new SecureRandom();
    private final HexFormat hexFormatter = HexFormat.of();

    private final AtomicLong counter = new AtomicLong();
    ShoppingBasket shoppingBasket = new ShoppingBasket();

    Map<String, ShoppingBasket> userCart = new HashMap<>();

    Map<String, String> sessions = new HashMap<>();

    String[] users = {"A", "B", "C", "D", "E"};

    public ShoppingController() {
        for (String userName : users) {
            userCart.put(userName, new ShoppingBasket());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam(value = "user", defaultValue = "") String user) {
        // We are just checking the username, in the real world you would also check their password here
        // or authenticate the user some other way.
        if (!Arrays.asList(users).contains(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user.\n");
        }

        // Generate the session token.
        byte[] sessionTokenBytes = new byte[16];
        randomNumberGenerator.nextBytes(sessionTokenBytes);
        String sessionToken = hexFormatter.formatHex(sessionTokenBytes);

        // Store the association of the session token with the user.
        sessions.put(sessionToken, user);

        // Create HTTP headers including the instruction for the browser to store the session token in a cookie.
        String setCookieHeaderValue = String.format("session=%s; Path=/; HttpOnly; SameSite=Strict;", sessionToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", setCookieHeaderValue);


/////////////
//        if (userCart.isEmpty()) {
//            for (String userName : users) {
//                userCart.put(userName, new ShoppingBasket());
//            }
//        }

/////////////

        // Redirect to the cart page, with the session-cookie-setting headers.
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).location(URI.create("/cart")).build();
    }


    @GetMapping("/cart")
    public String viewCart(@CookieValue(value = "session", defaultValue = "") String sessionToken, Model model) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);


        model.addAttribute("total", currentBasket.getCartTotal());
        model.addAttribute("sb", currentBasket);


        return "cart";

    }


    @PostMapping("/cart")
    public String updateCart(@RequestParam(value = "name", defaultValue = "") ArrayList<String> name,
                             @RequestParam(value = "qty", defaultValue = "") ArrayList<Integer> qty, Model model,
                             @RequestParam(value = "cost", defaultValue = "") ArrayList<String> cost,
                             @CookieValue(value = "session", defaultValue = "") String sessionToken) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb", currentBasket);

        //MIGHT NEED TO ADD A CHECK IF QTY < 0 OR > MAX_INT, AND REDIRECT TO ERROR PAGE.
        //SAME THING APPLIES TO OTHER METHODS IN THIS CLASS

        for (int i = 0; i < name.size(); i++) {
            if (qty.isEmpty() || qty.get(i) == null) {
                return "invalid";
            }
            if (qty.get(i) < 0) {
                return "invalid";
            }
            currentBasket.updateItemQty(name.get(i), qty.get(i));
        }

        return viewCart(sessionToken, model);

    }


    @GetMapping("/newname")
    public String newName(@RequestParam(value = "name", defaultValue = "") String name,
                          @RequestParam(value = "cost", defaultValue ="") String cost,
                          @CookieValue(value = "session", defaultValue = "") String sessionToken, Model model) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb", currentBasket);

        return "newname";
    }

    @PostMapping("/newname")
    public String newNamePost(@RequestParam(value = "name", defaultValue = "") String name,
                              @RequestParam(value = "cost", defaultValue = "") Double cost, Model model,
                              @CookieValue(value = "session", defaultValue = "") String sessionToken) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb", currentBasket);

        if (!currentBasket.addNewItem(name, cost)) {
            return "invalid";
        }
        return "newname";
    }


    @GetMapping("/delname")
    public String delItem(@RequestParam(value = "name", defaultValue = "") String name,
                          @RequestParam(value = "delete", defaultValue = "") String delete,
                          @CookieValue(value = "session", defaultValue = "") String sessionToken ,
                          Model model) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb",currentBasket);

        return "delname";
    }


    @PostMapping("/delname")
    public String delItemPost(@RequestParam(value = "item", defaultValue = "") ArrayList<String> name,
                              @RequestParam HashMap<String,String> delete,
                              @CookieValue(value = "session", defaultValue = "") String sessionToken,
                              Model model) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb", currentBasket);

        //delete items that are not in delete map, unchecked boxes will not add item to delete map
        for (int i = 0; i < name.size(); i++) {
            if (!delete.containsKey(name.get(i))) {
                if (!currentBasket.cartContainsItem(name.get(i))) {
                    return "invalid";
                }
                currentBasket.deleteItem(name.get(i));
            }
        }

        return viewCart(sessionToken, model);
    }


    @GetMapping("/updatename")
    public String updateName(@RequestParam(value = "name", defaultValue = "") ArrayList<String> name,
                             @RequestParam(value = "cost", defaultValue = "") ArrayList<String> cost,
                             @CookieValue(value = "session", defaultValue = "") String sessionToken ,
                             Model model) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb", currentBasket);

        return "updatename";
    }


    @PostMapping("/updatename")
    public String updateNamePost(@RequestParam(value = "name", defaultValue = "") ArrayList<String> name,
                             @RequestParam(value = "cost", defaultValue = "") ArrayList<Double> cost,
                             @RequestParam(value = "qty", defaultValue = "") ArrayList<Integer> qty,
                             @CookieValue(value = "session", defaultValue = "") String sessionToken,
                             Model model) {

        if (!sessions.containsKey(sessionToken)) {
            return "invalidId";
        }

        String loginId = sessions.get(sessionToken);
        ShoppingBasket currentBasket = userCart.get(loginId);

        model.addAttribute("sb", currentBasket);

        //check for duplicates in updated names
        Boolean hasDups = false;
        for (int i = 0; i < name.size() - 1; i++) {
            for (int j = i + 1; j < name.size(); j++) {
                if (name.get(i).equals(name.get(j))) {
                    hasDups = true;
                    break;
                }
            }
        }
        if (hasDups) {
            return "invalid";
        }

        ArrayList<Item> newItemsList = new ArrayList<>();

        for (int i = 0; i < name.size(); i++) {
            if (cost.get(i) == null || qty.get(i) == null || name.get(i).isEmpty() || qty.get(i) < 0 || cost.get(i) < 0 || cost.get(i) > Double.MAX_VALUE) {
                return "invalid";
            }
        }
        for (int i = 0; i < name.size(); i++) {
            newItemsList.add(new Item(name.get(i), cost.get(i), qty.get(i)));
        }


        currentBasket.setNewCart(newItemsList);

        return viewCart(sessionToken, model);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = "session", defaultValue = "") String sessionToken) {
        if (sessions.containsKey(sessionToken)) {
            // Clear the association of the session token with the user
            sessions.remove(sessionToken);

            // Remove the session token from the user's browser cookies
            String setCookieHeaderValue = "session=; Path=/; HttpOnly; SameSite=Strict; Max-Age=0;";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", setCookieHeaderValue);
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).location(URI.create("/")).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorised.\n");
        }
    }

}







