package de.strafbefehl.deluxehubreloaded.hook.hooks.head;

import de.strafbefehl.deluxehubreloaded.hook.PluginHook;
import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DatabaseHead implements PluginHook, HeadHook, Listener {

    private DeluxeHubReloadedPlugin plugin;
    private HeadDatabaseAPI api;

    @Override
    public void onEnable(DeluxeHubReloadedPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        api = new HeadDatabaseAPI();
    }

    @Override
    public ItemStack getHead(String data) {
        return api.getItemHead(data);
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent event) {
        plugin.getInventoryManager().onEnable(plugin);
    }

}
