package com.devnics.DiscordLink;

import com.devnics.DiscordLink.command.CommandLink;
import com.devnics.DiscordLink.listener.MessageListener;
import com.devnics.DiscordLink.listener.PlayerListeners;
import com.devnics.DiscordLink.listener.SlashCommandListener;
import com.devnics.DiscordLink.listener.StatusUpdater;
import com.devnics.DiscordLink.papi.DiscordLinkExpansion;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class DiscordLinkPlugin extends JavaPlugin {

    public HashMap<UUID, Integer> codes = new HashMap<>();
    public ArrayList<UUID> unverified = new ArrayList<>();

    public HashMap<String, DPlayer> accounts = new HashMap<>();
    public HashMap<UUID, String> players = new HashMap<>();

    public JDA bot;
    public Economy economy;

    private int code = 1000;

    public void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
        return;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic

        setupEconomy();
        saveDefaultConfig();

        JDABuilder builder = JDABuilder.createDefault(getConfig().getString("token"));

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")

        this.bot = builder.build();
        this.bot.addEventListener(new MessageListener(this));
        this.bot.addEventListener(new SlashCommandListener(this));
        this.bot.updateCommands().addCommands(
                Commands.slash("balance", "Check your points"),
                Commands.slash("shop", "Purchase items using your points")
        ).queue();

        new DiscordLinkExpansion(this).register();

        getCommand("link").setExecutor(
                new CommandLink(this)
        );

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
        getServer().getPluginManager().registerEvents(new StatusUpdater(this.bot), this);

        if (this.getConfig().getConfigurationSection("players") == null) return;

        for (String id: this.getConfig().getConfigurationSection("players").getKeys(false)) {
            System.out.println("Found: " + id);
            String discordId = this.getConfig().getString("players." + id);
            this.addAccount(
                    new DPlayer(
                            this,
                            UUID.fromString(id),
                            discordId
                    ),
                    discordId
            );

            this.addPlayer(UUID.fromString(id), discordId);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Integer generateCode(UUID uuid) {
        this.code = this.code + 1;
        this.codes.put(uuid, this.code);
        return this.code;
    }

    public void removeCode(UUID uuid) {
        this.codes.remove(uuid);
    }

    public void setUnverified(UUID uuid) {
        this.unverified.add(uuid);
    }

    public void setVerified(UUID uuid) {
        this.unverified.remove(uuid);
    }

    public void addAccount(DPlayer player, String id) {
        this.accounts.put(id, player);
        return;
    }

    public void addPlayer(UUID uuid, String id) {
        this.players.put(uuid, id);
    }

    public void removePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

}
