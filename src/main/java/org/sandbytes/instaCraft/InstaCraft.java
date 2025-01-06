package org.sandbytes.instaCraft;

import org.bukkit.plugin.java.JavaPlugin;
import org.sandbytes.instaCraft.commands.CraftCommand;
import org.sandbytes.instaCraft.commands.CraftTab;
import org.sandbytes.instaCraft.config.CraftConfig;

public class InstaCraft extends JavaPlugin {

    private static InstaCraft instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        CraftConfig.getInstance().load();
        loadCommands();

        getLogger().info("InstaCraft has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("InstaCraft has been disabled!");
    }

    private void loadCommands() {
        getCommand("craft").setExecutor(new CraftCommand(this));
        getCommand("craft").setTabCompleter(new CraftTab(this));
    }

    public static InstaCraft getInstance() {
        return instance;
    }
}
