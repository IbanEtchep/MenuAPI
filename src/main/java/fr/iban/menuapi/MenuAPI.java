package fr.iban.menuapi;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import fr.iban.menuapi.listeners.InventoryListener;

public final class MenuAPI {
	
    private static MenuAPI instance;
	
	private Plugin plugin;

    private MenuAPI(Plugin plugin){
    	this.plugin = plugin;
    	registerListeners(new InventoryListener());
    }
    
    public static MenuAPI getInstance(Plugin plugin) {
        return MenuAPI.instance == null ? new MenuAPI(plugin) : MenuAPI.instance;
    }
    
    public Plugin getPlugin() {
    	return this.plugin;
    }
    
    private void registerListeners(Listener... listeners) {
        PluginManager pm = Bukkit.getPluginManager();

        for (Listener listener : listeners) {
            pm.registerEvents(listener, plugin);
        }

    }


}
