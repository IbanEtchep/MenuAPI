package fr.iban.menuapi.menu;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.iban.menuapi.MenuAPI;
import fr.iban.menuapi.MenuItem;
import fr.iban.menuapi.objects.Display;

public abstract class ConfigurableMenu<T> extends PaginatedMenu {

	private boolean editMode;
	private T editing;
	private ItemStack pickup;

	
	protected ConfigurableMenu(Player player) {
		super(player);
	}

	public abstract String getMenuName();

	public abstract int getRows();

	@Override
	public boolean cancelClicks() {
		return !editMode;
	}

	@Override
	public void setMenuItems() {
		for(T item : getItems()) {
			Display display = getItemDisplay(item);
			setMenuItem(display.getSlot()+getSlots()*display.getPage(), getMenuItem(item));
		}
	}

	@Override
	public void handleMenuClick(InventoryClickEvent e) {
		if(e.getClick() == ClickType.SHIFT_LEFT && e.getClickedInventory() == e.getView().getBottomInventory() && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER && player.hasPermission("menuapi.editmode")) {
			editMode = !editMode;
			player.sendMessage("§aEdit mode : " + editMode);
			e.setCancelled(true);
			return;
		}
				
		/*
		 * In-game menu configuration
		 */
		if(editMode && !templateItems.containsKey(e.getSlot())){
			if(e.getClickedInventory() == e.getView().getTopInventory()) {
				switch (e.getAction()) {

					//Change item slot :
					case PICKUP_ALL:
					{
						T t = getItemAtSlot(e.getSlot());
						if(t != null) {
							editing = t;
							System.out.println(t);
						}else e.setCancelled(true);
						break;
					}
					case PLACE_ALL:
					case PLACE_ONE:
					{
						if(e.getCurrentItem() == null) {
							if(editing != null) {
								Display display = getItemDisplay(editing);
								display.setSlot(e.getSlot());
								display.setPage(page);
								setItemDisplay(editing, display);
							}else if(pickup != null){
								player.closeInventory();
								player.sendMessage("§2Entrez le nom que vous voulez donner à l'élément ajouté.");
								MenuAPI.getInstance().getTextInputs().put(player.getUniqueId(), text -> {
									Display display = new Display();
									display.setName(text);
									display.setPage(page);
									display.setSlot(e.getSlot());
									player.sendMessage(""+pickup.getType());
									display.setItemstack(pickup);
									addItem(display);
									player.sendMessage("§aUn nouvel élément a été ajouté au menu.");
									reload();
									MenuAPI.getInstance().getTextInputs().remove(player.getUniqueId());
								});
							}
						}
						break;
					}

					//Change item icon
					case SWAP_WITH_CURSOR:
					{
						ItemStack cursor = e.getCursor().clone();
						if(cursor != null && cursor.getType() != Material.AIR){
							T t = getItemAtSlot(e.getSlot());
							if(t != null) {
								Display display = getItemDisplay(t);
								player.sendMessage("§aChangement de l'icone : " +display.getItemstack().getType() + " -> " + cursor.getType());
								display.setItemstack(cursor);
								setItemDisplay(t, display);
								reload();
							}
						}
						break;
					}

					default:
						break;
				}
			}else if(e.getClickedInventory() == e.getView().getBottomInventory() && e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_ONE) {
				if(e.getCurrentItem() != null){
					player.sendMessage("Vous avez sélectionné " + e.getCurrentItem().getType());
					pickup = e.getCurrentItem().clone();
				}
			}
		}else {
			super.handleMenuClick(e);
		}
	}

	public abstract void setMenuTemplateItems();

	protected abstract Collection<T> getItems();

	protected abstract Display getItemDisplay(T object);

	protected abstract void setItemDisplay(T object, Display display);
	
	protected abstract void addItem(Display display);

	protected abstract MenuItem getMenuItem(T object);
	
	private T getItemAtSlot(int slot) {
		for(T t : getItems()) {
			Display display = getItemDisplay(t);
			if(display.getSlot() != slot || display.getPage() != page) continue;
			return t;
		}
		return null;
	}
}
