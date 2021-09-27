package fr.iban.menuapi.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import fr.iban.menuapi.MenuItem;
import fr.iban.menuapi.utils.ItemBuilder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;


public abstract class Menu implements InventoryHolder {

	protected Player player;
	protected Inventory inventory;
	protected ItemStack FILLER_GLASS = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("").build();
	protected Map<Integer, MenuItem> menuItems = new HashMap<>();
	protected Multimap<Integer, MenuItem> templateItems = ArrayListMultimap.create();
	protected List<Integer> templateSlots;
	protected Menu previousMenu;
	protected int page = 0;
	private boolean loaded = false;

	public Menu(Player player) {
		this.player = player;
	}

	public Menu(Player player, Menu previousMenu) {
		this(player);
		this.previousMenu = previousMenu;
	}

	public void open() {
		if(!loaded) {
			inventory = Bukkit.createInventory(this, getSlots(), LegacyComponentSerializer.builder().hexColors().build().deserialize(getMenuName()));
			this.setMenuTemplateItems();
			this.templateSlots= getTemplateSlots();
			this.setMenuItems();
			this.loaded = true;
		}
		inventory.clear();
		fillInventory();
		player.openInventory(inventory);
	}
	
	public void reload() {
		loaded = false;
		open();
	}
	
	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public abstract String getMenuName();

	public abstract int getRows();

	public int getSlots() {
		return getRows()*9;
	}

	
	/*
	 * Handle Events : 
	 */
	
	public void handleMenuClick(InventoryClickEvent e) {
		if(e.getClickedInventory() == e.getView().getTopInventory()) {
			int slot = page*getSlots()+e.getSlot();
			MenuItem item = menuItems.get(slot);
			if(item != null && item.getCallback() != null && item.getDisplayCondition().getAsBoolean()) {
				item.getCallback().onClick(e);
				return;
			}
			for(MenuItem templateItem : templateItems.get(e.getSlot())) {
				if(templateItem != null && templateItem.getCallback() != null && templateItem.getDisplayCondition().getAsBoolean()) {
					templateItem.getCallback().onClick(e);
				}
			}
		}
	}
	
	public boolean cancelClicks() {
		return true;
	}

	public void handleMenuClose(InventoryCloseEvent e) {

	}

	public abstract void setMenuItems();

	protected void fillTemplateItems() {
		for (int i = 0; i < getSlots(); i++) {
			for(MenuItem templateItem : templateItems.get(i)) {
				if(!templateItem.getDisplayCondition().getAsBoolean())
					continue;
				inventory.setItem(i, templateItem.getItem());
			}
		}
	}

	private void fillItems() {
		for (int i = 0; i < getSlots(); i++) {
			MenuItem menuItem = menuItems.get(i);
			if(menuItem == null) continue;
			inventory.setItem(i, menuItem.getItem());
		}
	}

	protected void fillInventory() {
		fillTemplateItems();
		fillItems();
	}


	protected MenuItem getCloseBotton(int slot) {
		if(hasPreviousMenu()) {
			return new MenuItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.DARK_RED + "Retour").build(), click -> {
				previousMenu.open();
			});
		}else {
			return new MenuItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.DARK_RED + "Fermer").build(), click -> {
				player.closeInventory();
			});
		}
	}

	protected boolean hasPreviousMenu() {
		return previousMenu != null;
	}

	private List<Integer> getTemplateSlots() {
		List<Integer> slots = new ArrayList<>();
		for (int i = 0; i < getSlots(); i++) {
			if(templateItems.containsKey(i)) {
				slots.add(i);
			}
		}
		return slots;
	}


	/*
	 * MenuItems methods
	 */

	protected void addMenuItem(int slot, ItemStack item){
		menuItems.put(slot, new MenuItem(slot, item));
	}

	protected void addMenuItem(MenuItem item) {
		menuItems.put(item.getSlot(), item);
	}

//	protected void addMenuItem(MenuItem item) {
//		int i = lastInsert;
//		while(menuItems.keySet().contains(i) || isTemplateSlot(i)) {
//			i++;
//		}
//		menuItems.put(i, item);
//		lastInsert = i;
//	}

	/*
	 * Menu template methods
	 */

	protected void setMenuTemplateItem(MenuItem item) {
		templateItems.put(item.getSlot(), item);
	}

	protected  void setMenuTemplateItem(int slot, ItemStack item){
		templateItems.put(slot, new MenuItem(slot, item));
	}

	public abstract void setMenuTemplateItems();

	public boolean isTemplateSlot(int slot) {
		return templateSlots.contains(slot) ;
	}

	/*
	 * Utils
	 */


	/**
	 * Util to cut a string into litle parts of defined size.
	 * @param msg
	 * @param lineSize
	 * @return
	 */
	protected List<String> splitString(String msg, int lineSize) {
		List<String> res = new ArrayList<>();
		Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
		Matcher m = p.matcher(msg);
		while(m.find()) {
			res.add("§a" + m.group());
		}
		return res;
	}

	public void setFillerGlass(){
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null){
				inventory.setItem(i, FILLER_GLASS);
			}
		}
	}

	public void fillWithGlass() {
		for (int i = inventory.firstEmpty() ; inventory.firstEmpty() != -1; i = inventory.firstEmpty()) {
			inventory.setItem(i, FILLER_GLASS);
		}
	}
	
	protected String centerTitle(String title) {
	    return Strings.repeat(" ", 22 - ChatColor.stripColor(title).length()) + title;
	}

	public void addMenuBorder(){
		for (int i = 0; i < 10; i++) {
			setMenuTemplateItem(new MenuItem(-1, i, FILLER_GLASS));
		}
		if(getRows() >= 2) {
			setMenuTemplateItem(9, FILLER_GLASS);
			setMenuTemplateItem(17, FILLER_GLASS);
		}
		if(getRows() >= 3) {
			setMenuTemplateItem(18, FILLER_GLASS);
			setMenuTemplateItem(26, FILLER_GLASS);
		}
		if(getRows() >= 4) {
			setMenuTemplateItem(35, FILLER_GLASS);
			setMenuTemplateItem(27, FILLER_GLASS);
		}
		if(getRows() >= 5) {
			setMenuTemplateItem(36, FILLER_GLASS);
			setMenuTemplateItem(44, FILLER_GLASS);
		}
		for (int i = getSlots() - 9; i < getSlots(); i++) {
			setMenuTemplateItem(i, FILLER_GLASS);
		}
	}

	protected void addItemAsync(MenuItem beforeLoaded, CompletableFuture<MenuItem> afterLoaded){
		addMenuItem(beforeLoaded);
		afterLoaded.thenAccept(item -> {
			addMenuItem(item);
			if(page == item.getPage() || item.getPage() == -1){
				inventory.setItem(item.getSlot(), item.getItem());
			}
		});
	}

}

