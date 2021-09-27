package fr.iban.menuapi.menu;

import fr.iban.menuapi.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.iban.menuapi.callbacks.ConfirmCallback;
import fr.iban.menuapi.utils.ItemBuilder;

public class ConfirmMenu extends Menu {
	
	private String title;
	private String desc;
	private ConfirmCallback callback;

	public ConfirmMenu(Player player, ConfirmCallback callback) {
		this(player, "§2Confirmer", "§aVoulez-vous vraiment faire cela?", callback);
	}
	
	public ConfirmMenu(Player player, String desc, ConfirmCallback callback) {
		this(player, "§2Confirmer", desc, callback);
	}
	
	public ConfirmMenu(Player player, String title, String desc, ConfirmCallback callback) {
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
		return new MenuItem(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§2§lCONFIRMER").build(), e -> callback.call(true));
	}
	
	private MenuItem getCancelItem(int slot) {
		return new MenuItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("§4§lANNULER").build(), e -> callback.call(false));
	}
	
	private MenuItem getMiddleItem(int slot) {
		return new MenuItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(desc).build());
	}

	@Override
	public void setMenuTemplateItems() {
		// TODO Auto-generated method stub
		
	}
	

}
