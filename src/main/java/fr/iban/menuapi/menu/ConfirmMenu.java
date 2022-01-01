package fr.iban.menuapi.menu;

import fr.iban.menuapi.menuitem.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.iban.menuapi.utils.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class ConfirmMenu extends Menu {
	
	private String title;
	private String desc;
	private Consumer<Boolean> callback;

	public ConfirmMenu(Player player, Consumer<Boolean> callback) {
		this(player, "§2Confirmer", "§aVoulez-vous vraiment faire cela?", callback);
	}
	
	public ConfirmMenu(Player player, String desc, Consumer<Boolean> callback) {
		this(player, "§2Confirmer", desc, callback);
	}
	
	public ConfirmMenu(Player player, String title, String desc, Consumer<Boolean> callback) {
		super(player);
		this.title = title;
		this.desc = desc;
		this.callback = callback;
	}

	@Override
	public String getMenuName() {
		return title;
	}

	@Override
	public int getRows() {
		return 3;
	}

	@Override
	public void setMenuItems() {
		for (int i = 0; i < getSlots(); i++) {
			int rowSlot = (i+9) % 9;
			if(rowSlot < 4) {
				addMenuItem(getConfirmItem(i));
			}else if(rowSlot > 4) {
				addMenuItem(getCancelItem(i));
			}else {
				addMenuItem(getMiddleItem(i));
			}
		}
	}

	private MenuItem getConfirmItem(int slot){
		return new MenuItem(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§2§lCONFIRMER").build())
				.setClickCallback(e -> callback.accept(true));
	}
	
	private MenuItem getCancelItem(int slot) {
		return new MenuItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("§4§lANNULER").build())
				.setClickCallback(e -> callback.accept(false));
	}
	
	private MenuItem getMiddleItem(int slot) {
		return new MenuItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(desc).build());
	}
	

}
