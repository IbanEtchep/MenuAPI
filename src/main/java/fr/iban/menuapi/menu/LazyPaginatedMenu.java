package fr.iban.menuapi.menu;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.iban.menuapi.MenuItem;
import fr.iban.menuapi.utils.ItemBuilder;


public abstract class LazyPaginatedMenu<T> extends PaginatedMenu {

	private List<T> lazyObjectList;

	protected LazyPaginatedMenu(Player player) {
		super(player);
		this.lazyObjectList = getLazyObjectList();
	}

	@Override
	protected MenuItem getNextBotton() {
		return new MenuItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Suivant").build(), click -> {
			page += 1;
			open();
		}, () -> (!(lazyObjectList.isEmpty() || lazyObjectList.size() <= (getSlots()-templateSlots.size())) || page != getLastPage()));
	}

	/*
	 * MenuItems
	 */

	@Override
	protected void fillInventory() {
		fillTemplateItems();

		int startslot = page*getSlots();
		int endslot = (page+1)*getSlots();

		for (int i = startslot; i < endslot; i++) {
			int slot = i - page*getSlots();
			MenuItem menuItem = menuItems.get(i);
			if(menuItem == null || !menuItem.getDisplayCondition().getAsBoolean()) continue;
			inventory.setItem(slot, menuItem.getItem());
			slot++;
		}

		int firstEmpty = inventory.firstEmpty();
		while(firstEmpty != -1 && !lazyObjectList.isEmpty()) {
			T object = lazyObjectList.get(0);
			MenuItem item = getMenuItem(object);
			if(item == null || !item.getDisplayCondition().getAsBoolean()) continue;
			menuItems.put(firstEmpty+getSlots()*page, item);
			inventory.setItem(firstEmpty, item.getItem());
			lazyObjectList.remove(0);
			firstEmpty = inventory.firstEmpty();
		}
	}

	protected abstract MenuItem getMenuItem(T object);

	protected abstract List<T> getLazyObjectList();

}

