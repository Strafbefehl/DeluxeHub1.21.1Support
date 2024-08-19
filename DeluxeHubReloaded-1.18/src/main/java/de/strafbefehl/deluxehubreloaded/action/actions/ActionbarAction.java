package de.strafbefehl.deluxehubreloaded.action.actions;

import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import de.strafbefehl.deluxehubreloaded.action.Action;
import de.strafbefehl.deluxehubreloaded.utility.TextUtil;
import de.strafbefehl.deluxehubreloaded.utility.reflection.ActionBar;
import org.bukkit.entity.Player;

public class ActionbarAction implements Action {

    @Override
    public String getIdentifier() {
        return "ACTIONBAR";
    }

    @Override
    public void execute(DeluxeHubReloadedPlugin plugin, Player player, String data) {
        ActionBar.sendActionBar(player, TextUtil.color(data));
    }
}
