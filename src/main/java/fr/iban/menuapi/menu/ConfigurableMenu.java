package fr.iban.menuapi.menu;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.iban.menuapi.MenuAPI;
import fr.iban.menuapi.menuitem.MenuItem;
import fr.iban.menuapi.menuitem.ConfigurableItem;

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
	
	protected abstract Collection<T> getItems();

	protected abstract ConfigurableItem getConfigurableItem(T object);

	protected abstract void setItemDisplay(T object, ConfigurableItem display);

	protected abstract void addItem(ConfigurableItem display);

	protected abstract void removeItem(T item);

	protected abstract MenuItem getMenuItem(T object);

	private T getItemAtSlot(int slot) {
		for(T t : getItems()) {
			ConfigurableItem display = getConfigurableItem(t);
			if(display.getSlot() != slot || display.getPage() != page) continue;
			return t;
		}
		return null;
	}

	@Override
	public void setMenuItems() {
		for(T item : getItems()) {
			addMenuItem(getMenuItem(item));
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
							}else e.setCancelled(true);
							break;
						}
						case PLACE_ALL:
						case PLACE_ONE:
						{
							if(e.getCurrentItem() == null) {
								if(editing != null) {
									ConfigurableItem configurableItem = getConfigurableItem(editing);
									configurableItem.setSlot(e.getSlot());
									configurableItem.setPage(page);
									setItemDisplay(editing, configurableItem);
								}else if(pickup != null){
									player.closeInventory();
									ConfigurableItem display = new ConfigurableItem();
									display.setPage(page);
									display.setSlot(e.getSlot());
									display.setItemstack(pickup);

									if(e.getAction() == InventoryAction.PLACE_ALL){
										player.sendMessage("§2Entrez le nom que vous voulez donner à l'élément ajouté.");
										MenuAPI.getInstance().getTextInputs().put(player.getUniqueId(), text -> {
											display.setName(text);
											addItem(display);
											player.sendMessage("§aUn nouvel élément a été ajouté au menu.");
											reload();
											MenuAPI.getInstance().getTextInputs().remove(player.getUniqueId());
										});
									}else{
										display.setName(" ");
										display.setLore(null);
										addItem(display);
										player.sendMessage("§aUn nouvel élément a été ajouté au menu.");
										reload();
										MenuAPI.getInstance().getTextInputs().remove(player.getUniqueId());
									}
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
									ConfigurableItem configurableItem = getConfigurableItem(t);
									player.sendMessage("§aChangement de l'icone : " +configurableItem.getItemStack().getType() + " -> " + cursor.getType());
									configurableItem.setItemstack(cursor);
									setItemDisplay(t, configurableItem);
									reload();
								}
							}
							break;
						}
						default:
							break;
					}
			}else if(e.getClickedInventory() == e.getView().getBottomInventory()) {
				if(e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.PICKUP_ONE){
					if(e.getCurrentItem() != null){
						player.sendMessage("Vous avez sélectionné " + e.getCurrentItem().getType());
						pickup = e.getCurrentItem().clone();
					}
				}
			}
			if(e.getAction() == InventoryAction.DROP_ALL_CURSOR || e.getAction() == InventoryAction.DROP_ONE_CURSOR){
				if(editing != null){
					new ConfirmMenu(player, "Suprimer l'item?", confirmed -> {
						if(confirmed){
							removeItem(editing);
							reload();
						}else{
							open();
						}
					}).open();
				}
			}
		}else {
			super.handleMenuClick(e);
		}
	}
}
