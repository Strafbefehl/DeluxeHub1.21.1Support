package de.strafbefehl.deluxehubreloaded.action.actions;

import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import de.strafbefehl.deluxehubreloaded.utility.TextUtil;
import de.strafbefehl.deluxehubreloaded.action.Action;
import org.bukkit.entity.Player;

public class MessageAction implements Action {

    @Override
    public String getIdentifier() {
        return "MESSAGE";
    }

    @Override
    public void execute(DeluxeHubReloadedPlugin plugin, Player player, String data) {
        if (data.contains("<center>") && data.contains("</center>")) data = TextUtil.getCenteredMessage(data);
        player.sendMessage(TextUtil.color(data));
    }
}
