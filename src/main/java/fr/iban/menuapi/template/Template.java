package fr.iban.menuapi.template;

import fr.iban.menuapi.MenuItem;

import java.util.Map;

public class Template {

    private String name;
    private Map<Integer, MenuItem> menuItems;

    public Template(String name, Map<Integer, MenuItem> menuItems){
        this.menuItems = menuItems;
        this.name = name;
    }

    public Map<Integer, MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getName() {
        return name;
    }
}
