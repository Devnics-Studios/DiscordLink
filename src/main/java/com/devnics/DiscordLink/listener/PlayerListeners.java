package com.devnics.DiscordLink.listener;

import com.devnics.DiscordLink.DPlayer;
import com.devnics.DiscordLink.DiscordLinkPlugin;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;

public class PlayerListeners implements Listener {

    private DiscordLinkPlugin plugin;

    public PlayerListeners(DiscordLinkPlugin discordLinkPlugin) {
        this.plugin = discordLinkPlugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!this.plugin.players.containsKey(event.getPlayer().getUniqueId())) {
            if (this.plugin.getConfig().contains("players." + event.getPlayer().getUniqueId())) {
                this.plugin.addPlayer(event.getPlayer().getUniqueId(), this.plugin.getConfig().getString("players." + event.getPlayer().getUniqueId()));
                return;
            }
            event.getPlayer().sendMessage(
                    ChatColor.RED + "Please connect your discord account using /link."
            );
            event.getPlayer().sendTitle(ChatColor.RED + "/link", ChatColor.YELLOW + "Connect your discord", 20, 20 * 5, 20 * 2);
            this.plugin.setUnverified(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent event) {
        if (this.plugin.unverified.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        if (this.plugin.players.containsKey(event.getPlayer().getUniqueId())) {
            this.plugin.removePlayer(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (this.plugin.unverified.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(
                    ChatColor.RED + "Please connect your discord account using /link."
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (this.plugin.unverified.contains(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHotBarChange(PlayerItemHeldEvent event) {
        if (this.plugin.unverified.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (this.plugin.unverified.contains(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (this.plugin.unverified.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/link")) return;
        if (this.plugin.unverified.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
