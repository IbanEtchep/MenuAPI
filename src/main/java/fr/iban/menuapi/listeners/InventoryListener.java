package fr.iban.menuapi.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

import fr.iban.menuapi.menu.Menu;

public class InventoryListener implements Listener {
	
    @EventHandler
    public void onMenuClick(InventoryClickEvent e){
    	
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            e.setCancelled(menu.cancelClicks());
            menu.handleMenuClick(e);
        }

    }
    
    @EventHandler
    public void onMenuClose(InventoryCloseEvent e){

        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu menu) {
            menu.handleMenuClose(e);
        }

    }

}
