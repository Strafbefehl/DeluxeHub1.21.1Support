package de.strafbefehl.deluxehubreloaded.module.modules.chat;

import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import de.strafbefehl.deluxehubreloaded.Permissions;
import de.strafbefehl.deluxehubreloaded.config.ConfigType;
import de.strafbefehl.deluxehubreloaded.config.Messages;
import de.strafbefehl.deluxehubreloaded.module.Module;
import de.strafbefehl.deluxehubreloaded.module.ModuleType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class AntiSwear extends Module {

    private List<String> blockedWords;

    public AntiSwear(DeluxeHubReloadedPlugin plugin) {
        super(plugin, ModuleType.ANTI_SWEAR);
    }

    @Override
    public void onEnable() {
        blockedWords = getConfig(ConfigType.SETTINGS).getStringList("anti_swear.blocked_words");
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        if (player.hasPermission(Permissions.ANTI_SWEAR_BYPASS.getPermission())) return;

        String message = event.getMessage();

        for (String word : blockedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {

                event.setCancelled(true);
                Messages.ANTI_SWEAR_WORD_BLOCKED.send(player);

                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission(Permissions.ANTI_SWEAR_NOTIFY.getPermission())).forEach(p -> {
                    Messages.ANTI_SWEAR_ADMIN_NOTIFY.send(p,"%player%", player.getName(),"%word%", message);
                });

                return;
            }
        }
    }
}
