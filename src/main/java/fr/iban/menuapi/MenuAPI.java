package fr.iban.menuapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.iban.menuapi.objects.Template;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.iban.menuapi.callbacks.TextCallback;
import fr.iban.menuapi.listeners.AsyncChatListener;
import fr.iban.menuapi.listeners.InventoryListener;
import fr.iban.menuapi.objects.Display;

public final class MenuAPI extends JavaPlugin {
	
	private static MenuAPI instance;
	private Map<UUID, TextCallback> textInputs;
	private Map<String, Template> templates;

	@Override
	public void onEnable() {
		instance = this;
		textInputs = new HashMap<>();
		templates = getTemplatesFromConfig();
		saveDefaultConfig();
    	ConfigurationSerialization.registerClass(Display.class, "display");
    	registerListeners(new InventoryListener(), new AsyncChatListener(this));
	}
	
    private void registerListeners(Listener... listeners) {
        PluginManager pm = Bukkit.getPluginManager();

        for (Listener listener : listeners) {
            pm.registerEvents(listener, this);
        }

    }
    
    public Map<UUID, TextCallback> getTextInputs() {
		return textInputs;
	}

	public static MenuAPI getInstance() {
		return instance;
	}

	private Map<String, Template> getTemplatesFromConfig(){
		Map<String, Template> templates = new HashMap<>();
		if(getConfig().getConfigurationSection("menus") != null){
			for(String name : getConfig().getConfigurationSection("menus").getKeys(false)){
				String configPath = "menus."+name;
				Map<Integer, MenuItem> map = new HashMap<>();
				for(int slot : getConfig().getConfigurationSection(configPath+".items").getKeys(false).stream().map(s -> Integer.parseInt(s)).toList()){
					map.put(slot, new MenuItem(((Display) getConfig().get(configPath+".items."+slot)).getBuiltItemStack()));
				}
				templates.put(name, new Template(name, map));
			}
		}
		return templates;
	}

	public Map<String, Template> getTemplates() {
		return templates;
	}
}
