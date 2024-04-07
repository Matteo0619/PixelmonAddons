package me.poskemon.it.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import me.poskemon.it.PixelmonAddons;
import me.poskemon.it.config.MessageConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

public class ChangeGenderCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        MessageConfig messageConfig = PixelmonAddons.getMessageConfig();

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("changegender")
                .executes(context -> {
                    context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderNoSlot()));
                    return 0;
                })
                .then(Commands.argument("slot", StringArgumentType.greedyString())
                        .executes(context -> {

                            ServerPlayerEntity player;

                            try {
                                player = context.getSource().getPlayerOrException();
                            } catch (Exception e) {
                                context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderInvalidSource()));
                                return 0;
                            }

                            if(!PermissionAPI.hasPermission(player, "pixelmonaddons.command.changegender")) {
                                StringTextComponent message = new StringTextComponent(messageConfig.getChangeGenderNoPermission());
                                message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                context.getSource().sendFailure(message);
                                return 0;
                            }

                            int slot;

                            try {
                                slot = Integer.parseInt(context.getArgument("slot", String.class));
                            } catch (Exception e) {
                                context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderInvalidNumber()));
                                return 0;
                            }

                            if(slot < 1 || slot > 6) {
                                context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderInvalidSlot()));
                                return 0;
                            }

                            PlayerPartyStorage party = StorageProxy.getParty(player);

                            if(party.get(slot-1) == null) {
                                context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderEmptySlot()));
                                return 0;
                            }

                            Pokemon pokemon = party.get(slot-1);

                            if(pokemon.isEgg()) {
                                context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderInvalidPokemon()));
                                return 0;
                            }

                            Stats stat = pokemon.getForm();

                            Gender gender = pokemon.getGender();

                            if(Gender.MALE.equals(gender) && stat.getMalePercentage() < 100f) {
                                pokemon.setGender(Gender.FEMALE);
                            } else if(Gender.FEMALE.equals(gender) && stat.getMalePercentage() > 0f) {
                                pokemon.setGender(Gender.MALE);
                            } else {
                                context.getSource().sendFailure(new StringTextComponent(messageConfig.getChangeGenderInvalidPokemon()));
                                return 0;
                            }

                            StringTextComponent success = new StringTextComponent(messageConfig.getChangeGenderSuccess());
                            success.setStyle(success.getStyle().withColor(TextFormatting.GREEN));

                            context.getSource().sendSuccess(success, false);
                            return 1;
                        })
                )

        );


    }

}
