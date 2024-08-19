package de.strafbefehl.deluxehubreloaded;

import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import de.strafbefehl.deluxehubreloaded.action.ActionManager;
import de.strafbefehl.deluxehubreloaded.command.CommandManager;
import de.strafbefehl.deluxehubreloaded.config.ConfigManager;
import de.strafbefehl.deluxehubreloaded.config.ConfigType;
import de.strafbefehl.deluxehubreloaded.config.Messages;
import de.strafbefehl.deluxehubreloaded.cooldown.CooldownManager;
import de.strafbefehl.deluxehubreloaded.hook.HooksManager;
import de.strafbefehl.deluxehubreloaded.inventory.InventoryManager;
import de.strafbefehl.deluxehubreloaded.module.ModuleManager;
import de.strafbefehl.deluxehubreloaded.module.ModuleType;
import de.strafbefehl.deluxehubreloaded.module.modules.hologram.HologramManager;
import de.strafbefehl.deluxehubreloaded.utility.UpdateChecker;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class DeluxeHubReloadedPlugin extends JavaPlugin {

    private static final int BSTATS_ID = 23061;

    private ConfigManager configManager;
    private ActionManager actionManager;
    private HooksManager hooksManager;
    private CommandManager commandManager;
    private CooldownManager cooldownManager;
    private ModuleManager moduleManager;
    private InventoryManager inventoryManager;

    public void onEnable() {
        long start = System.currentTimeMillis();

        getLogger().log(Level.INFO, " ··························································································");
        getLogger().log(Level.INFO, " : ____       _                _   _       _     ____      _                 _          _ :");
        getLogger().log(Level.INFO, " :|  _ \\  ___| |_   ___  _____| | | |_   _| |__ |  _ \\ ___| | ___   __ _  __| | ___  __| |:");
        getLogger().log(Level.INFO, " :| | | |/ _ \\ | | | \\ \\/ / _ \\ |_| | | | | '_ \\| |_) / _ \\ |/ _ \\ / _` |/ _` |/ _ \\/ _` |:");
        getLogger().log(Level.INFO, " :| |_| |  __/ | |_| |>  <  __/  _  | |_| | |_) |  _ <  __/ | (_) | (_| | (_| |  __/ (_| |:");
        getLogger().log(Level.INFO, " :|____/ \\___|_|\\__,_/_/\\_\\___|_| |_|\\__,_|_.__/|_| \\_\\___|_|\\___/ \\__,_|\\__,_|\\___|\\__,_|:");
        getLogger().log(Level.INFO, " ··························································································");
        getLogger().log(Level.INFO, "");
        getLogger().log(Level.INFO, "Version: " + getDescription().getVersion());
        getLogger().log(Level.INFO, "Author: Strafbefehl and ItsLewizzz (Old Developer)");
        getLogger().log(Level.INFO, "");

        // Check if using Spigot
        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException ex) {
            getLogger().severe("============= SPIGOT NOT DETECTED =============");
            getLogger().severe("DeluxeHubReloaded requires Spigot to run, you can download");
            getLogger().severe("Spigot here: https://www.spigotmc.org/wiki/spigot-installation/.");
            getLogger().severe("The plugin will now disable.");
            getLogger().severe("============= SPIGOT NOT DETECTED =============");
            getPluginLoader().disablePlugin(this);
            return;
        }

        MinecraftVersion.disableUpdateCheck();

        // Enable bStats metrics
        new MetricsLite(this, BSTATS_ID);

        // Check plugin hooks
        hooksManager = new HooksManager(this);

        // Load config files
        configManager = new ConfigManager();
        configManager.loadFiles(this);

        // If there were any configuration errors we should not continue
        if (!getServer().getPluginManager().isPluginEnabled(this)) return;

        // Command manager
        commandManager = new CommandManager(this);
        commandManager.reload();

        // Cooldown manager
        cooldownManager = new CooldownManager();

        // Inventory (GUI) manager
        inventoryManager = new InventoryManager();
        if (!hooksManager.isHookEnabled("HEAD_DATABASE")) inventoryManager.onEnable(this);

        // Core plugin modules
        moduleManager = new ModuleManager();
        moduleManager.loadModules(this);

        // Action system
        actionManager = new ActionManager(this);

        // Load update checker (if enabled)
        if (getConfigManager().getFile(ConfigType.SETTINGS).getConfig().getBoolean("check-updates"))
            new UpdateChecker(this).checkForUpdate();

        // Register BungeeCord channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getLogger().log(Level.INFO, "");
        getLogger().log(Level.INFO, "Successfully loaded in " + (System.currentTimeMillis() - start) + "ms");
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        moduleManager.unloadModules();
        inventoryManager.onDisable();
        configManager.saveFiles();

    }

    public void reload() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        configManager.reloadFiles();

        inventoryManager.onDisable();
        inventoryManager.onEnable(this);

        getCommandManager().reload();

        moduleManager.loadModules(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        try {
            getCommandManager().execute(cmd.getName(), args, sender);
        } catch (CommandPermissionsException e) {
            Messages.NO_PERMISSION.send(sender);
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            //sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + "Usage: " + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An internal error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    public HologramManager getHologramManager() {
        return (HologramManager) moduleManager.getModule(ModuleType.HOLOGRAMS);
    }

    public HooksManager getHookManager() {
        return hooksManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }
}