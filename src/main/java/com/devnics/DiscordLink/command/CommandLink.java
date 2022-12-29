package com.devnics.DiscordLink.command;

import com.devnics.DiscordLink.DiscordLinkPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandLink implements CommandExecutor {

    private DiscordLinkPlugin plugin;

    public CommandLink(DiscordLinkPlugin discordLinkPlugin) {
        this.plugin = discordLinkPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (this.plugin.codes.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You already have a code pending.");
            return true;
        }

        if (this.plugin.accounts.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are already linked!");
            return true;
        }

        int code = this.plugin.generateCode(player.getUniqueId());

        player.sendMessage(
                ChatColor.RED + "" + ChatColor.BOLD + "Valid for 5mins",
                ChatColor.translateAlternateColorCodes(
                        '&',
                        "&aDM the following code to &e{D}&a: &e{C}"
                                .replace("{D}", this.plugin.bot.getSelfUser().getAsTag())
                                .replace("{C}", Integer.toString(code))
                )
        );
        return true;
    }
}
