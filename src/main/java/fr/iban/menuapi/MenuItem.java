package fr.iban.menuapi;

import java.util.function.BooleanSupplier;

import org.bukkit.inventory.ItemStack;

import fr.iban.menuapi.callbacks.ClickCallback;

public class MenuItem {

	private int slot;
	private int page;
	private ItemStack item;
	private ClickCallback callback;
	private BooleanSupplier displayCondition;
	private ConfigurableItem configurableItem;

	/*
	Unpaginated menu item (will be displayed in every pages if paginated menu)
	 */
	public MenuItem(int slot, ItemStack item) {
		this.slot = slot;
		this.page = -1;
		this.item = item;
	}

	public MenuItem(int slot, ItemStack item, ClickCallback callback) {
		this(-1, slot, item);
		this.callback = callback;
	}

	public MenuItem(int slot, ItemStack item, BooleanSupplier displayCondition) {
		this(-1, slot, item);
		this.displayCondition = displayCondition;
	}

	public MenuItem(int slot, ItemStack item, ClickCallback callback, BooleanSupplier displayCondition) {
		this(-1, slot, item, callback);
		this.displayCondition = displayCondition;
	}
	public MenuItem(ConfigurableItem item){
		this(item.getPage(), item.getSlot(), item.getItemstack());
		this.configurableItem = item;
	}

	public MenuItem(ConfigurableItem item, ClickCallback callback){
		this(item);
		this.callback = callback;
	}

	public MenuItem(ConfigurableItem item, ClickCallback callback, BooleanSupplier displayCondition){
		this(item, callback);
		this.displayCondition = displayCondition;
	}

	/*
	Paginated menu item
	 */
	public MenuItem(int page, int slot, ItemStack item) {
		this.slot = slot;
		this.page = page;
		this.item = item;
	}
	
	public MenuItem(int page, int slot, ItemStack item, ClickCallback callback) {
		this(page, slot, item);
		this.callback = callback;
	}
	
	public MenuItem(int page, int slot, ItemStack item, BooleanSupplier displayCondition) {
		this(page, slot, item);
		this.displayCondition = displayCondition;
	}
	
	public MenuItem(int page, int slot, ItemStack item, ClickCallback callback, BooleanSupplier displayCondition) {
		this(page, slot, item, callback);
		this.displayCondition = displayCondition;
	}

	public MenuItem(int page, int slot, ConfigurableItem item){
		this(page, slot, item.getItemstack());
		this.configurableItem = item;
	}

	public ClickCallback getCallback() {
		return callback;
	}
	
	public ItemStack getItem() {
		return configurableItem == null ? item : configurableItem.getBuiltItemStack();
	}

	/**
	 *
	 * @return page where item is displayed;
	 */
	public int getPage() {
		return configurableItem == null ? page : configurableItem.getPage();
	}

	/**
	 *
	 * @return slot where item is displayed
	 */
	public int getSlot() {
		return configurableItem == null ? slot : configurableItem.getSlot();
	}

	public BooleanSupplier getDisplayCondition() {
		if(displayCondition == null) {
			return () -> true;
		}
		return displayCondition;
	}
}
