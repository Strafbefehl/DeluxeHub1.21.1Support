package de.strafbefehl.deluxehubreloaded.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import de.strafbefehl.deluxehubreloaded.config.Messages;
import de.strafbefehl.deluxehubreloaded.module.ModuleType;
import de.strafbefehl.deluxehubreloaded.module.modules.player.PlayerVanish;
import de.strafbefehl.deluxehubreloaded.DeluxeHubReloadedPlugin;
import de.strafbefehl.deluxehubreloaded.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand {

    private DeluxeHubReloadedPlugin plugin;

    public VanishCommand(DeluxeHubReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"vanish"},
            desc = "Disappear into thin air!"
    )
    public void vanish(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(Permissions.COMMAND_VANISH.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot set the spawn location.");
            return;
        }

        Player player = (Player) sender;
        PlayerVanish vanishModule = ((PlayerVanish) plugin.getModuleManager().getModule(ModuleType.VANISH));
        vanishModule.toggleVanish(player);
    }

}
