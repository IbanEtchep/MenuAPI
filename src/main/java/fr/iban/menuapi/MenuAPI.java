package fr.iban.menuapi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.iban.menuapi.commands.MenuApiCMD;
import fr.iban.menuapi.template.TemplateManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.iban.menuapi.callbacks.TextCallback;
import fr.iban.menuapi.listeners.AsyncChatListener;
import fr.iban.menuapi.listeners.InventoryListener;

public final class MenuAPI extends JavaPlugin {
	
	private static MenuAPI instance;
	private Map<UUID, TextCallback> textInputs;
	private TemplateManager templateManager;

	@Override
	public void onEnable() {
		instance = this;
		textInputs = new HashMap<>();
		saveDefaultConfig();
    	ConfigurationSerialization.registerClass(ConfigurableItem.class, "menuitem");
		this.templateManager = new TemplateManager(this);
		getCommand("menuapi").setExecutor(new MenuApiCMD(this));
    	registerListeners(new InventoryListener(), new AsyncChatListener(this));
	}
	
    private void registerListeners(Listener... listeners) {
        PluginManager pm = Bukkit.getPluginManager();

        for (Listener listener : listeners) {
            pm.registerEvents(listener, this);
        }

    }

	public static MenuAPI getInstance() {
		return instance;
	}

	public Map<UUID, TextCallback> getTextInputs() {
		return textInputs;
	}

	public TemplateManager getTemplateManager() {
		return templateManager;
	}


}
