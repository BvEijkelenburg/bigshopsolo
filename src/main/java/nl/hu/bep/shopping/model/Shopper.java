package nl.hu.bep.shopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Shopper implements NamedObject, Principal {
    private String name;
    private String password;
    private String role;
    private static List<Shopper> allShoppers = new ArrayList<>();
    private List<ShoppingList> allLists = new ArrayList<>();

    public Shopper(String nm) {
        this.name = nm;
        this.password = "secret";
        this.role = "admin";
        if (!allShoppers.contains(this)) allShoppers.add(this);
    }

    public Shopper(String nm, String password, String role) {
        this.name = nm;
        this.password = password;
        this.role = role;
        if (!allShoppers.contains(this)) allShoppers.add(this);
    }

    public boolean checkPassword(String passwordToCheck) {
        return passwordToCheck.equals(password);
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shopper shopper = (Shopper) o;
        return name.equals(shopper.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public static List<Shopper> getAllShoppers() {
        return Collections.unmodifiableList(allShoppers);
    }

    public boolean addList(ShoppingList newList) {
        if (!allLists.contains(newList)) {
            return allLists.add(newList);
        }
        return false;
    }

    @JsonIgnore
    public List<ShoppingList> getAllLists() {
        return Collections.unmodifiableList(allLists);
    }

    public int getAmountOfLists() {
        return allLists.size();
    }
}
