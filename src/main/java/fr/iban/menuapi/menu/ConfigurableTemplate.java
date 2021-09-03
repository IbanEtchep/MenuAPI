package fr.iban.menuapi.menu;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import fr.iban.menuapi.MenuItem;
import fr.iban.menuapi.objects.Display;

public class ConfigurableTemplate extends ConfigurableMenu<Integer>{

	private @NotNull FileConfiguration config;
	private String configPath;
	private String name;
	private Plugin plugin;

	public ConfigurableTemplate(Player player, Plugin plugin, String name) {
		super(player);
		this.name = name;
		this.plugin = plugin;
		this.config = plugin.getConfig();
		this.configPath = "menus."+name;
	}

	@Override
	public String getMenuName() {
		return name;
	}

	@Override
	public int getRows() {
		return config.getInt(configPath+".menurows", 6);
	}

	@Override
	public void setMenuTemplateItems() {
		
	}

	@Override
	protected Collection<Integer> getItems() {
		return config.getConfigurationSection(configPath+".items").getKeys(false).stream().map(s -> Integer.parseInt(s)).toList();
	}

	@Override
	protected Display getItemDisplay(Integer integer) {
		return (Display) config.get(configPath+".items."+integer);
	}

	@Override
	protected void setItemDisplay(Integer integer, Display display) {
		config.set(configPath+".items."+integer, display);
		plugin.saveConfig();
	}

	@Override
	protected MenuItem getMenuItem(Integer integer) {
		return new MenuItem(getItemDisplay(integer).getBuiltItemStack());
	}
	
	public Map<Integer, MenuItem> getMenuItems() {
		return menuItems;
	}

	@Override
	protected void addItem(Display display) {
		int id = Collections.max(getItems()) + 1;
		setItemDisplay(id, display);
	}

}
