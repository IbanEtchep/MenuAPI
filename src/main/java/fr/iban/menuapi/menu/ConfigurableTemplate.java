package fr.iban.menuapi.menu;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import fr.iban.menuapi.template.Template;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import fr.iban.menuapi.menuitem.MenuItem;
import fr.iban.menuapi.menuitem.ConfigurableItem;

public class ConfigurableTemplate extends ConfigurableMenu<Integer>{

	private final @NotNull FileConfiguration config;
	private final String configPath;
	private final String name;
	private final Plugin plugin;

	public ConfigurableTemplate(Player player, Plugin plugin, Template template) {
		super(player);
		this.name = template.getName();
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
	protected Collection<Integer> getItems() {
		return config.getConfigurationSection(configPath+".items").getKeys(false).stream().map(Integer::parseInt).toList();
	}

	@Override
	protected ConfigurableItem getConfigurableItem(Integer integer) {
		return (ConfigurableItem) config.get(configPath+".items."+integer);
	}

	@Override
	protected void setItemDisplay(Integer integer, ConfigurableItem display) {
		config.set(configPath+".items."+integer, display);
		plugin.saveConfig();
	}

	@Override
	protected MenuItem getMenuItem(Integer integer) {
		return getConfigurableItem(integer);
	}

	@Override
	protected void addItem(ConfigurableItem display) {
		int id = Collections.max(getItems()) + 1;
		setItemDisplay(id, display);
	}

	@Override
	protected void removeItem(Integer integer) {
		config.set(configPath+".items."+integer, null);
		plugin.saveConfig();
	}

}
