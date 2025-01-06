package org.sandbytes.instaCraft.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.sandbytes.instaCraft.InstaCraft;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CraftConfig {

    private final static CraftConfig instance = new CraftConfig();

    private File file;
    private static YamlConfiguration config;

    public void load() {
        file = new File(InstaCraft.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            InstaCraft.getInstance().saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
        config.options().parseComments(true);

        InputStream defConfigStream = InstaCraft.getInstance().getResource("config.yml");
        if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
                config.setDefaults(defConfig);
                config.options().copyDefaults(true);
        }

        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            InstaCraft.getInstance().getLogger().severe("An error occured while saving the config file!");
        }
    }

    public static void reloadConfig() {
        CraftConfig.getInstance().load();
    }

    public static String getMessage(String message) {
        return config.getString(message);
    }

    public static CraftConfig getInstance() {
        return instance;
    }

}
