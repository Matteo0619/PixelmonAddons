package me.poskemon.it.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;


public class ResetEvsCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("resetevs")
                        .executes(context -> {
                            context.getSource().sendFailure(new StringTextComponent("You need to specify a slot!"));
                            return 0;
                        })
                        .then(Commands.argument("slot", StringArgumentType.greedyString())
                                .executes(context -> {

                                    if(!PermissionAPI.hasPermission(context.getSource().getPlayerOrException(), "pixelmonaddons.command.resetevs")) {
                                        StringTextComponent message = new StringTextComponent("You do not have access to this command!");
                                        message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                        context.getSource().sendFailure(message);
                                        return 0;
                                    }

                                    int slot;

                                    try {
                                        slot = Integer.parseInt(context.getArgument("slot", String.class));
                                    } catch (Exception e) {
                                        context.getSource().sendFailure(new StringTextComponent("Invalid number"));
                                        return 0;
                                    }

                                    if(slot < 1 || slot > 6) {
                                        context.getSource().sendFailure(new StringTextComponent("Invalid slot"));
                                        return 0;
                                    }

                                    ServerPlayerEntity player;

                                    try {
                                        player = context.getSource().getPlayerOrException();
                                    } catch (Exception e) {
                                        context.getSource().sendFailure(new StringTextComponent("This is a player only command!"));
                                        return 0;
                                    }

                                    PlayerPartyStorage party = StorageProxy.getParty(player);

                                    if(party.get(slot-1) == null) {
                                        context.getSource().sendFailure(new StringTextComponent("The input slot is empty"));
                                        return 0;
                                    }

                                    Pokemon pokemon = party.get(slot-1);

                                    for(BattleStatsType evs : BattleStatsType.getEVIVStatValues()) {
                                        pokemon.getEVs().setStat(evs, 0);
                                    }

                                    StringTextComponent success = new StringTextComponent("EVS correctly reset!");
                                    success.setStyle(success.getStyle().withColor(TextFormatting.GREEN));

                                    context.getSource().sendSuccess(success, false);
                                    return 1;
                                })
                        )

        );


    }
}
