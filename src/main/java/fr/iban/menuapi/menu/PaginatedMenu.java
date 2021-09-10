package fr.iban.menuapi.menu;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.iban.menuapi.MenuItem;
import fr.iban.menuapi.utils.ItemBuilder;


public abstract class PaginatedMenu extends Menu {

	protected int page = 0;

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

		MenuItem nextBotton = getNextBotton();
		setMenuTemplateItem(lastRowFirst+5, nextBotton);
		//setMenuTemplateItem(lastRowFirst+5,new MenuItem(FILLER_GLASS, () -> !nextBotton.getDisplayCondition().getAsBoolean()));

		MenuItem prevBotton = getPreviousBotton();
		setMenuTemplateItem(lastRowFirst+3, prevBotton);
		//setMenuTemplateItem(lastRowFirst+3,new MenuItem(FILLER_GLASS, () -> !prevBotton.getDisplayCondition().getAsBoolean()));


		setMenuTemplateItem(lastRowFirst+4, getCloseBotton());
	}

	protected MenuItem getNextBotton() {
		return new MenuItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Suivant").build(), click -> {
			page += 1;
			open();
		}, () -> page != getLastPage());
	}

	protected MenuItem getPreviousBotton() {
		return new MenuItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Précédent").build(), click -> {
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


	@Override
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

	protected int getLastPage() {
		if(menuItems.isEmpty()) return 0;
		return Collections.max(menuItems.keySet())/getSlots();
	}

	protected void setMenuItem(int page, int row, int coll, MenuItem item) {
		int slot = row*9 + coll;
		setMenuItem(page, slot, item);
	}

	@Override
	protected void setMenuItem(int page, int slot, MenuItem item) {
		setMenuItem(page*getSlots()+slot, item);
	}
}

