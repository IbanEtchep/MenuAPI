package fr.iban.menuapi.menu;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.iban.menuapi.MenuItem;
import fr.iban.menuapi.utils.ItemBuilder;


public abstract class PaginatedMenu extends Menu {

	protected PaginatedMenu(Player player) {
		super(player);
	}

	@Override
	public void open() {
		super.open();
	}

	@Override
	public boolean isTemplateSlot(int slot) {
		int slotpage = slot/getSlots();
		int realslot = slot - slotpage*getSlots();
		return super.isTemplateSlot(realslot);
	}

	protected void addMenuBottons() {
		int lastRowFirst = (getRows()-1)*9;

		setMenuTemplateItem(getNextBotton(lastRowFirst+5));
		//setMenuTemplateItem(lastRowFirst+5,new MenuItem(FILLER_GLASS, () -> !nextBotton.getDisplayCondition().getAsBoolean()));

		setMenuTemplateItem(getPreviousBotton(lastRowFirst+3));
		//setMenuTemplateItem(lastRowFirst+3,new MenuItem(FILLER_GLASS, () -> !prevBotton.getDisplayCondition().getAsBoolean()));

		setMenuTemplateItem(getCloseBotton(lastRowFirst+4));
	}

	protected MenuItem getNextBotton(int slot) {
		return new MenuItem(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Suivant").build(), click -> {
			page += 1;
			open();
		}, () -> page != getLastPage());
	}

	protected MenuItem getPreviousBotton(int slot) {
		return new MenuItem(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Précédent").build(), click -> {
			page -= 1;
			open();
		}, () -> page != 0);
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
	}


	protected int getLastPage() {
		if(menuItems.isEmpty()) return 0;
		return Collections.max(menuItems.keySet())/getSlots();
	}
}

