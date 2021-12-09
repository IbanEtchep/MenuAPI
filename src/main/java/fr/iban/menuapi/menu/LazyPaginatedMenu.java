package fr.iban.menuapi.menu;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.iban.menuapi.menuitem.MenuItem;
import fr.iban.menuapi.utils.ItemBuilder;


public abstract class LazyPaginatedMenu<T> extends PaginatedMenu {

	private List<T> lazyObjectList;
	private int lastPage;

	protected LazyPaginatedMenu(Player player) {
		super(player);
		this.lazyObjectList = getLazyObjectList();
		lastPage = lazyObjectList.size()/getFillableSlots().length;
	}

	protected abstract int[] getFillableSlots();

	/*
	 * MenuItems
	 */
	@Override
	protected void fillInventory() {
		super.fillInventory();
		for(int slot : getFillableSlots()){
			if(lazyObjectList.isEmpty()) break;
			if(inventory.getItem(slot) != null) continue;
			T object = lazyObjectList.get(0);
			MenuItem item = getMenuItem(object);
			if(item == null || !item.isDisplayable()) continue;
			menuItems.put(slot+getSlots()*page, item);
			inventory.setItem(slot, item.getItemStack());
			lazyObjectList.remove(0);
		}
	}

	protected abstract MenuItem getMenuItem(T object);

	protected abstract List<T> getLazyObjectList();

	@Override
	protected int getLastPage() {
		return Math.max(super.getLastPage(), lastPage);
	}
}

