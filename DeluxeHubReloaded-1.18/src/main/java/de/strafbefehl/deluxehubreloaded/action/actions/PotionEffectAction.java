package de.strafbefehl.deluxehubreloaded.action.actions;

import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import de.strafbefehl.deluxehubreloaded.action.Action;
import de.strafbefehl.deluxehubreloaded.utility.universal.XPotion;
import org.bukkit.entity.Player;

public class PotionEffectAction implements Action {

    @Override
    public String getIdentifier() {
        return "EFFECT";
    }

    @Override
    public void execute(DeluxeHubReloadedPlugin plugin, Player player, String data) {
        String[] args = data.split(";");
        player.addPotionEffect(XPotion.matchXPotion(args[0]).get().parsePotion(1000000, Integer.parseInt(args[1]) - 1));
    }
}
