package com.devnics.DiscordLink.listener;

import com.devnics.DiscordLink.DPlayer;
import com.devnics.DiscordLink.DiscordLinkPlugin;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class MessageListener extends ListenerAdapter {

    private DiscordLinkPlugin plugin;

    public MessageListener(DiscordLinkPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild()) return;
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();

        if (message.getContentRaw() == null) return;

        String content = message.getContentRaw();

        if (!isInteger(content)) {
            event.getChannel().sendMessage(
                    "Oops! That's not a valid code."
            ).queue();
            return;
        }

        int code = Integer.parseInt(content);

        if (!this.plugin.codes.containsValue(code)) {
            event.getChannel().sendMessage(
                    "Oops! That's not a valid code."
            ).queue();
            return;
        }

        Set<UUID> uuids = this.plugin.codes.keySet();
        UUID uuid = null;

        for (UUID id: uuids) {
            if (this.plugin.codes.get(id) == code) {
                uuid = id;
                break;
            }
        }

        if (uuid == null) {
            event.getChannel().sendMessage(
                    "Oops! Your account could not be linked"
            ).queue();
            return;
        }

        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            event.getChannel().sendMessage(
                    "You need to be online on the server to link your account."
            ).queue();
            return;
        }

        this.plugin.removeCode(uuid);

        this.plugin.getConfig().set("players." + uuid, event.getAuthor().getId());
        this.plugin.saveConfig();

        this.plugin.addAccount(
                new DPlayer(
                        this.plugin,
                        player.getUniqueId(),
                        event.getAuthor().getId()
                ),
                event.getAuthor().getId()
        );

        this.plugin.addPlayer(player.getUniqueId(), event.getAuthor().getId());

        this.plugin.setVerified(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);

        event.getChannel().sendMessage(
                "Verified! Your account is now linked with **{}**"
                        .replace("{}", player.getName())
        ).queue();
        player.sendMessage(
                ChatColor.GREEN + "Your account is now linked with {}".replace("{}", event.getAuthor().getName())
        );
    }
    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

}
