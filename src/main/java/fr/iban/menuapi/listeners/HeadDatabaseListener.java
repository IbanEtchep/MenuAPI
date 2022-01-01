package fr.iban.menuapi.listeners;

import fr.iban.menuapi.utils.Head;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDatabaseListener implements Listener {
	
    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
    	Head.loadAPI();
    }

}
