package de.strafbefehl.deluxehubreloaded.config;

import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private Map<ConfigType, ConfigHandler> configurations;

    public ConfigManager() {
        configurations = new HashMap<>();
    }

    public void loadFiles(DeluxeHubReloadedPlugin plugin) {

        registerFile(ConfigType.SETTINGS, new ConfigHandler(plugin, "config"));
        registerFile(ConfigType.MESSAGES, new ConfigHandler(plugin, "messages"));
        registerFile(ConfigType.DATA, new ConfigHandler(plugin, "data"));
        registerFile(ConfigType.COMMANDS, new ConfigHandler(plugin, "commands"));

        configurations.values().forEach(ConfigHandler::saveDefaultConfig);

        Messages.setConfiguration(getFile(ConfigType.MESSAGES).getConfig());
    }

    public ConfigHandler getFile(ConfigType type) {
        return configurations.get(type);
    }

    public void reloadFiles() {
        configurations.values().forEach(ConfigHandler::reload);
        Messages.setConfiguration(getFile(ConfigType.MESSAGES).getConfig());
    }

    public void saveFiles() {
        getFile(ConfigType.DATA).save();
    }

    public void registerFile(ConfigType type, ConfigHandler config) {
        configurations.put(type, config);
    }

    public FileConfiguration getFileConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

}
