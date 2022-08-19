package fr.iban.menuapi.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.iban.menuapi.MenuAPI;

public class AsyncChatListener implements Listener {

	private final MenuAPI plugin;

	public AsyncChatListener(MenuAPI plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if(plugin.getTextInputs().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().runTask(plugin, () -> plugin.getTextInputs().get(player.getUniqueId()).accept(e.getMessage()));
			e.setCancelled(true);
			return;
		}
	}
}