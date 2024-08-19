package de.strafbefehl.deluxehubreloaded.action.actions;

import de.strafbefehl.deluxehubreloaded.action.Action;
import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import de.strafbefehl.deluxehubreloaded.inventory.AbstractInventory;
import org.bukkit.entity.Player;

public class MenuAction implements Action {

    @Override
    public String getIdentifier() {
        return "MENU";
    }

    @Override
    public void execute(DeluxeHubReloadedPlugin plugin, Player player, String data) {
        AbstractInventory inventory = plugin.getInventoryManager().getInventory(data);

        if (inventory != null) {
            inventory.openInventory(player);
        } else {
            plugin.getLogger().warning("[MENU] Action Failed: Menu '" + data + "' not found.");
        }
    }
}
