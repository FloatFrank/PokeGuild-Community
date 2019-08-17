package com.piaofu.pokeguild.guild.guildgift;


import catserver.api.bukkit.event.ForgeEvent;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.side.PokemonSide;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.piaofu.pokeguild.guild.GuildTools;

@PokemonSide
public class GiftListenerForPokemon implements Listener {
    /**
     * 击败BOSS给予奖励
     * @param event
     */
    @EventHandler
    public static void onKillBossPokemon(ForgeEvent event) {

        if(!(event.getForgeEvent() instanceof BeatWildPixelmonEvent)) {
            return;
        }
        BeatWildPixelmonEvent battleEndEvent;
        battleEndEvent = (BeatWildPixelmonEvent) event.getForgeEvent();
        Guild guild;
        if((guild = GuildTools.getPlayerGuild(battleEndEvent.player.getPersistentID())) == null)
            return;
        int sum = 0;
        int add = DataLoader.getKillBoss();
        int add2 = DataLoader.getKillNormal();
        for (PixelmonWrapper pixelmonWrapper : battleEndEvent.wpp.allPokemon) {
            if(pixelmonWrapper.entity.isBossPokemon()) {
                sum += add;
            }else {
                sum += add2;
            }
        }
        if(sum!=0)
        GuildTools.giveCon(guild, sum, battleEndEvent.player.getPersistentID());
    }
}
