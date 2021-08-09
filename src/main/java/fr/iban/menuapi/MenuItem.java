package fr.iban.menuapi;

import java.util.function.BooleanSupplier;

import org.bukkit.inventory.ItemStack;

import fr.iban.menuapi.callbacks.ClickCallback;

public class MenuItem {
	
	private ItemStack item;
	private ClickCallback callback;
	private BooleanSupplier displayCondition;
	
	public MenuItem(ItemStack item) {
		this.item = item;
	}
	
	public MenuItem(ItemStack item, ClickCallback callback) {
		this(item);
		this.callback = callback;
	}
	
	public MenuItem(ItemStack item, BooleanSupplier displayCondition) {
		this(item);
		this.displayCondition = displayCondition;
	}
	
	public MenuItem(ItemStack item, ClickCallback callback, BooleanSupplier displayCondition) {
		this(item, callback);
		this.displayCondition = displayCondition;
	}
	
	public MenuItem(MenuItem item) {
		this(item.getItem(), item.getCallback());
	}

	public ClickCallback getCallback() {
		return callback;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public BooleanSupplier getDisplayCondition() {
		if(displayCondition == null) {
			return () -> true;
		}
		return displayCondition;
	}
}
