package fr.iban.menuapi.menu;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.iban.menuapi.menuitem.MenuItem;
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

		MenuItem nextButton = getNextBotton();
		if(nextButton.getSlot() == -1){
			nextButton.setSlot(lastRowFirst+5);
		}
		setMenuTemplateItem(nextButton);

		MenuItem prevButton = getPreviousBotton();
		if(prevButton.getSlot() == -1){
			prevButton.setSlot(lastRowFirst+3);
		}
		setMenuTemplateItem(getPreviousBotton().setSlot(lastRowFirst+3));
		//setMenuTemplateItem(lastRowFirst+3,new MenuItem(FILLER_GLASS, () -> !prevBotton.getDisplayCondition().getAsBoolean()));

		setMenuTemplateItem(getCloseBotton(lastRowFirst+4));
	}

	protected MenuItem getNextBotton() {
		return new MenuItem(-1, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Suivant").build())
				.setClickCallback(click -> {
					page += 1;
					open();
				})
				.setDisplayCondition(() -> page != getLastPage());
	}

	protected MenuItem getPreviousBotton() {
		return new MenuItem(-1, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Précédent").build())
				.setClickCallback(click -> {
					page -= 1;
					open();
				}).setDisplayCondition(() -> page != 0);
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
			if(menuItem == null || !menuItem.isDisplayable()) continue;
			inventory.setItem(slot, menuItem.getItemStack());
			slot++;
		}
	}


	protected int getLastPage() {
		if(menuItems.isEmpty()) return 0;
		return Collections.max(menuItems.keySet())/getSlots();
	}
}

