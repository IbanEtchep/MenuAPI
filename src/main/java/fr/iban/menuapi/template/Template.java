package fr.iban.menuapi.template;

import fr.iban.menuapi.Display;

import java.util.Map;

public class Template {

    private String name;
    //          ID       DISPLAY
    private Map<Integer, Display> displays;

    public Template(String name, Map<Integer, Display> displays){
        this.displays = displays;
        this.name = name;
    }

    public Map<Integer, Display> getDisplays() {
        return displays;
    }

    public String getName() {
        return name;
    }
}
