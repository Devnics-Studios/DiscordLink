package com.devnics.DiscordLink.listener;

import com.devnics.DiscordLink.DPlayer;
import com.devnics.DiscordLink.DiscordLinkPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;

public class SlashCommandListener extends ListenerAdapter {

    private DiscordLinkPlugin plugin;

    public SlashCommandListener(DiscordLinkPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("balance")) {
            User user = event.getUser();
            DPlayer dPlayer = this.plugin.accounts.get(user.getId());

            EmbedBuilder embed = new EmbedBuilder();

            if (dPlayer == null) {
                embed.setColor(Color.RED);
                embed.setTitle("No linked account");
                embed.setDescription("Could not find an account linked with your discord.");
            } else {
                embed.setColor(Color.ORANGE);
                embed.setTitle("Coins");
                embed.setDescription("$" + dPlayer.getBalance());
            }

            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
        }

        if (event.getName().equalsIgnoreCase("shop")) {
            User user = event.getUser();

            DPlayer dPlayer = this.plugin.accounts.get(user.getId());

            EmbedBuilder embed = new EmbedBuilder();

            if (dPlayer == null) {
                embed.setColor(Color.RED);
                embed.setTitle("No linked account");
                embed.setDescription("Could not find an account linked with your discord.");
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                return;
            } else {
                embed.setColor(Color.ORANGE);
                embed.setTitle("Championship Store");
                embed.setDescription(
                        String.join("\n", Arrays.asList(
                                ":moneybag: **Your Points:** $" + dPlayer.getBalance()
                        ))
                ); // Load items from config
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            }
        }
    }

}
