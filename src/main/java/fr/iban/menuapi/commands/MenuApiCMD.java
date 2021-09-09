package fr.iban.menuapi.commands;

import fr.iban.menuapi.MenuAPI;
import fr.iban.menuapi.menu.ConfigurableTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MenuApiCMD implements CommandExecutor {

    private MenuAPI plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length == 0){
                player.sendMessage("/menuapi edittemplate <name>");
            }
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("edittemplate")){
                    String templateName = args[1];
                    new ConfigurableTemplate(player, plugin, plugin.getTemplates().get(templateName)).open();
                }
            }
        }
        return false;
    }
}
