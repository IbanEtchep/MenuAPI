package fr.iban.menuapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	@Override
	public void onEnable() {
		instance = this;
		textInputs = new HashMap<>();
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

	public Map<Integer, MenuItem> getTemplateItems(String template){
		String configPath = "menus."+template;
		Map<Integer, MenuItem> map = new HashMap<>();
		for(int slot : getConfig().getConfigurationSection(configPath+".items").getKeys(false).stream().map(s -> Integer.parseInt(s)).toList()){
			map.put(slot, new MenuItem(((Display) getConfig().get(configPath+".items."+slot)).getBuiltItemStack()));
		}
		return map;
	}

}
