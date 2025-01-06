package org.sandbytes.instaCraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sandbytes.instaCraft.InstaCraft;
import org.sandbytes.instaCraft.config.CraftConfig;
import org.sandbytes.instaCraft.utils.CraftingUtils;

public class CraftCommand implements CommandExecutor {

    private InstaCraft plugin;

    public CraftCommand(InstaCraft instance) {
        this.plugin = instance;
    }

    private static final String help_message = "&6&lInstaCraft"
            + "\n&6/craft <item> &f- &eInstantly craft an item"
            + "\n&6/craft reload &f- &eReload the config.yml";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    help_message));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("instacraft.reload")) {
                CraftConfig.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CraftConfig.getMessage("messages.reload_success")));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CraftConfig.getMessage("messages.no_permission")
                                .replace("%permission%", "instacraft.reload")));
            }
            return true;
        }

        if (sender.hasPermission("instacraft.use")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length != 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            CraftConfig.getMessage("messages.usage")
                                    .replace("%command%", "/craft <item>")));
                    return true;
                }

                Material material = Material.matchMaterial(args[0]);
                if (material == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            CraftConfig.getMessage("messages.invalid_item")
                                    .replace("%item%", args[0])));
                    return true;
                }

                if (!CraftingUtils.canBeCrafted(material)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            CraftConfig.getMessage("messages.cannot_craft")
                                    .replace("%item%", material.name())));
                    return true;
                }

                if (CraftingUtils.hasRequiredMaterials(player, material)) {
                    CraftingUtils.craftItem(player, material);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            CraftConfig.getMessage("messages.crafted_success")
                                    .replace("%item%", material.name())));
                    return true;
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            CraftConfig.getMessage("messages.missing_materials")
                                    .replace("%item%", material.name())));
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CraftConfig.getMessage("messages.player_only")));
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    CraftConfig.getMessage("messages.no_permission")
                            .replace("%permission%", "instacraft.use")));
        }
        return true;
    }
}
