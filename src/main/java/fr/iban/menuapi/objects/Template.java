package fr.iban.menuapi.objects;

import fr.iban.menuapi.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template {

    private String name;
    private Map<Integer, MenuItem> menuItems;

    public Template(String name, Map<Integer, MenuItem> menuItems){
        this.menuItems = menuItems;
    }

    public Map<Integer, MenuItem> getMenuItems() {
        return menuItems;
    }
}
