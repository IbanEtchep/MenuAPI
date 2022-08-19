package fr.iban.menuapi.menuitem;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class MenuItem {

	protected int page = -1;
	protected int slot = -1;
	protected ItemStack itemstack;
	protected Consumer<InventoryClickEvent> clickCallback;
	protected BooleanSupplier displayCondition;

	/*
	Unpaginated menu item (will be displayed in every pages if paginated menu)
	 */
	public MenuItem(int slot, ItemStack itemstack) {
		this.slot = slot;
		this.itemstack = itemstack;
	}

	/*
	Paginated menu item
	 */
	public MenuItem(int page, int slot, ItemStack itemstack) {
		this.slot = slot;
		this.page = page;
		this.itemstack = itemstack;
	}

	public MenuItem() {

	}

	public MenuItem setClickCallback(Consumer<InventoryClickEvent> callback){
		this.clickCallback = callback;
		return this;
	}

	public MenuItem setDisplayCondition(BooleanSupplier displayCondition) {
		this.displayCondition = displayCondition;
		return this;
	}

	public void onClick(InventoryClickEvent e){
		if(clickCallback != null && isDisplayable()){
			clickCallback.accept(e);
		}
	}

	public boolean isDisplayable(){
		if(displayCondition != null){
			return displayCondition.getAsBoolean();
		}
		return true;
	}

	public MenuItem setSlot(int slot){
		this.slot = slot;
		return this;
	}

	public ItemStack getItemStack(){
		return itemstack;
	}

	public int getSlot() {
		return slot;
	}

	public int getPage() {
		return page;
	}


}
