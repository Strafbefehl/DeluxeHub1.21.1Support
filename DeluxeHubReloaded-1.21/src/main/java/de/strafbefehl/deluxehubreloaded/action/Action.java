package de.strafbefehl.deluxehubreloaded.action;

import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import org.bukkit.entity.Player;

public interface Action {

    String getIdentifier();

    void execute(DeluxeHubReloadedPlugin plugin, Player player, String data);

}
