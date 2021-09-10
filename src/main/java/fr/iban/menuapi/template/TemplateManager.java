package fr.iban.menuapi.template;

import fr.iban.menuapi.Display;
import fr.iban.menuapi.MenuAPI;
import fr.iban.menuapi.MenuItem;
import org.bukkit.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class TemplateManager {

    private MenuAPI plugin;
    private Map<String, Template> templates;
    private Configuration config;


    public TemplateManager(MenuAPI plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        templates = getTemplatesFromConfig();
    }


    private Map<String, Template> getTemplatesFromConfig(){
        Map<String, Template> templates = new HashMap<>();
        if(config.getConfigurationSection("menus") != null){
            for(String name : config.getConfigurationSection("menus").getKeys(false)){
                String configPath = "menus."+name;
                Map<Integer, Display> map = new HashMap<>();
                for(int slot : config.getConfigurationSection(configPath+".items").getKeys(false).stream().map(s -> Integer.parseInt(s)).toList()){
                    map.put(slot, ((Display) config.get(configPath+".items."+slot)));
                }
                templates.put(name, new Template(name, map));
            }
        }
        return templates;
    }

    public void createTemplate(String name){
        config.set("menus."+name+".items.0", new Display());
        templates = getTemplatesFromConfig();
    }

    public Template getTemplate(String name) {
        return templates.getOrDefault(name, new Template("boutique", new HashMap<>()));
    }

    public Map<String, Template> getTemplates() {
        return templates;
    }
}
