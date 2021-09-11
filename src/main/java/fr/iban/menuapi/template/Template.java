package fr.iban.menuapi.template;

import fr.iban.menuapi.ConfigurableItem;

import java.util.Map;

public class Template {

    private String name;
    //          ID       DISPLAY
    private Map<Integer, ConfigurableItem> displays;

    public Template(String name, Map<Integer, ConfigurableItem> displays){
        this.displays = displays;
        this.name = name;
    }

    public Map<Integer, ConfigurableItem> getDisplays() {
        return displays;
    }

    public String getName() {
        return name;
    }
}
