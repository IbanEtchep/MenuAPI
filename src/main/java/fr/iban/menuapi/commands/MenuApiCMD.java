package fr.iban.menuapi.commands;

import fr.iban.menuapi.MenuAPI;
import fr.iban.menuapi.template.TemplateManager;
import fr.iban.menuapi.menu.ConfigurableTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuApiCMD implements CommandExecutor {

    private MenuAPI plugin;

    public MenuApiCMD(MenuAPI plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player player){
            if(args.length == 0){
                player.sendMessage("/menuapi opentemplate <name>");
            }
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("opentemplate")){
                    String templateName = args[1];
                    TemplateManager templateManager = plugin.getTemplateManager();
                    if(!templateManager.getTemplates().containsKey(templateName)){
                        templateManager.createTemplate(templateName);
                    }
                    new ConfigurableTemplate(player, plugin, plugin.getTemplateManager().getTemplate(templateName)).open();
                }
            }
        }
        return false;
    }
}
