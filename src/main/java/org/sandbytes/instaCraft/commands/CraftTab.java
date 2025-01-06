package org.sandbytes.instaCraft.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sandbytes.instaCraft.InstaCraft;

import java.util.ArrayList;
import java.util.List;

public class CraftTab implements TabCompleter {

    private InstaCraft plugin;

    public CraftTab(InstaCraft instance) {
        this.plugin = instance;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            String search = args[0].toLowerCase();
            for (Material material : Material.values()) {
                if (material.isItem() && material.name().toLowerCase().startsWith(search)) {
                    suggestions.add(material.name().toLowerCase());
                }
            }
        }

        return suggestions;
    }
}
