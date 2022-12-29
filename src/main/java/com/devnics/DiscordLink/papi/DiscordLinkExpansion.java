package com.devnics.DiscordLink.papi;

import com.devnics.DiscordLink.DPlayer;
import com.devnics.DiscordLink.DiscordLinkPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class DiscordLinkExpansion extends PlaceholderExpansion {

    private DiscordLinkPlugin plugin;

    public DiscordLinkExpansion(DiscordLinkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "discordlink";
    }

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    @Override
    public @NotNull String getAuthor() {
        return "Ivan";
    }

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("linked_account")){
            String discordId = this.plugin.players.get(player.getUniqueId());

            if (discordId == null) return "None";

            DPlayer dPlayer = this.plugin.accounts.get(discordId);

            if (dPlayer == null) return "None";

            String username = dPlayer.discordUser.getName();

            if (username == null) return "None";

            return username;
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
