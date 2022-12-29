package com.devnics.DiscordLink.listener;// Import the necessary libraries
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StatusUpdater implements Listener {
    private JDA jda;

    public StatusUpdater(JDA jda) {
        this.jda = jda;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateStatus();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        updateStatus();
    }

    private void updateStatus() {
        // Update the bot's status to show the current number of players
        jda.getPresence().setActivity(Activity.watching(String.format("%d players", Bukkit.getServer().getOnlinePlayers().size())));
    }
}
