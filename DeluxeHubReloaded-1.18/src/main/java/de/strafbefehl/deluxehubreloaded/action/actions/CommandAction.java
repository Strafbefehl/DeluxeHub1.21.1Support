package de.strafbefehl.deluxehubreloaded.action.actions;

import de.strafbefehl.deluxehubreloaded.action.Action;
import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import org.bukkit.entity.Player;

public class CommandAction implements Action {

    @Override
    public String getIdentifier() {
        return "COMMAND";
    }

    @Override
    public void execute(DeluxeHubReloadedPlugin plugin, Player player, String data) {
        player.chat(data.contains("/") ? data : "/" + data);
    }
}
