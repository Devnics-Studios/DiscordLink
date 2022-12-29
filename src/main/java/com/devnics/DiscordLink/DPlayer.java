package com.devnics.DiscordLink;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DPlayer {

    private DiscordLinkPlugin plugin;

    public User discordUser;

    public String discordId;
    public UUID uuid;

    public DPlayer(DiscordLinkPlugin plugin, UUID uuid, String discordId) {
        this.plugin = plugin;
        this.discordId = discordId;
        this.uuid = uuid;

        this.discordUser = this.plugin.bot.retrieveUserById(discordId).complete();
    }

    public double getBalance() {
        OfflinePlayer player = Bukkit.getPlayer(uuid);

        if (player == null) {
            player = Bukkit.getOfflinePlayer(this.uuid);
        }

        return this.plugin.economy.getBalance(player);
    }
}
