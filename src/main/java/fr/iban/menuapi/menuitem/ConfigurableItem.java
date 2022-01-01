package fr.iban.menuapi.menuitem;

import fr.iban.menuapi.utils.HexColor;
import fr.iban.menuapi.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SerializableAs("menuitem")
public class ConfigurableItem extends MenuItem implements ConfigurationSerializable {

	private String name;
	private List<String> lore;
	private boolean enchanted = false;
	private List<String> clickCommands = new ArrayList<>();
	private String clickPermission;


	public ConfigurableItem(int page, int slot, ItemStack itemstack, String name, List<String> lore, boolean enchanted, List<String> clickCommands, String clickPermission) {
		super();
		this.page = page;
		this.slot = slot;
		this.itemstack = removeItemMeta(itemstack);
		this.name = name;
		this.lore = lore;
		this.enchanted = enchanted;
		this.clickCommands = clickCommands;
		this.clickPermission = clickPermission;
	}

	public ConfigurableItem(ConfigurableItem item){
		this(item.getPage(), item.getSlot(), item.getItemStack(), item.getName(), item.getLore(), item.isEnchanted(), item.getClickCommands(), item.getClickPermission());
	}

	@SuppressWarnings("unchecked")
	public ConfigurableItem(Map<String, Object> map) {
		super();
		if(map.containsKey("page")) {
			page = (int) map.get("page");
		}
		if(map.containsKey("slot")) {
			slot = (int) map.get("slot");
		}
		if(map.containsKey("item")) {
			itemstack = (ItemStack) map.get("item");
		}
		if(map.containsKey("name")) {
			name = (String) map.get("name");
		}
		if(map.containsKey("lore")) {
			lore = (List<String>) map.get("lore");
		}
		if(map.containsKey("commands")) {
			clickCommands = (List<String>) map.get("commands");
		}
		if(map.containsKey("enchanted")) {
			enchanted = (boolean)map.get("enchanted");
		}
		if(map.containsKey("clickpermission")){
			this.clickPermission = (String) map.get("clickpermission");
		}
	}

	public ConfigurableItem() {
		this(-1, -1, new ItemStack(Material.DIRT), "No name", Arrays.asList("NoDesc", "Nodesc"), false, null, null);
	}

	@Override
	public void onClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if(clickPermission != null && !player.hasPermission(clickPermission)){
			player.sendMessage("§cVous n'avez pas la permission de cliquer sur ce bouton.");
			return;
		}

		if(clickCommands.isEmpty()){
			for(String command : getClickCommands()){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
			}
		}
		super.onClick(e);
	}

	@Override
	public boolean isDisplayable() {
		return super.isDisplayable();
	}


	@Override
	public ItemStack getItemStack() {
		return new ItemBuilder(itemstack).setName(HexColor.translateColorCodes(name)).setLore(lore == null ? new ArrayList<>() : HexColor.translateColorCodes(lore)).setGlow(enchanted).build();
	}

	public void setItemstack(ItemStack item) {
		this.itemstack = removeItemMeta(item);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSlot() {
		return slot;
	}

	public boolean isEnchanted() {
		return enchanted;
	}

	public List<String> getClickCommands() {
		return clickCommands;
	}

	public String getClickPermission() {
		return clickPermission;
	}

	@Override
	public @NotNull Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("page", getPage());
		map.put("slot", getSlot());
		map.put("item", removeItemMeta(itemstack));
		map.put("name", getName());
		map.put("lore", getLore());
		map.put("enchanted", isEnchanted());
		if(clickCommands != null && !clickCommands.isEmpty()){
			map.put("commands", clickCommands);
		}
		if(clickPermission != null){
			map.put("clickpermission", clickPermission);
		}
		return map;
	}

	/*
		Remove item meta to avoid duplicated meta information on config file.
	 */
	private ItemStack removeItemMeta(ItemStack itemstack){
		ItemStack item = itemstack.clone();
		if (item.hasItemMeta()) {
			ItemMeta im = item.getItemMeta();
			if (im.hasLore())
				im.setLore(new ArrayList<>());
			if (im.hasDisplayName())
				im.setDisplayName("");
			if(itemstack.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
				im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
				enchanted = true;
				if(im.hasEnchants()){
					for(Enchantment enchant : im.getEnchants().keySet()){
						im.removeEnchant(enchant);
					}
				}
			}
			item.setItemMeta(im);
		}
		return item;
	}

}
