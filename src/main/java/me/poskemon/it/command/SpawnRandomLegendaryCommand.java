package me.poskemon.it.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.poskemon.it.PixelmonAddons;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.LocationInput;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

public class SpawnRandomLegendaryCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("randomlegendary")
                .executes(context -> {
                    context.getSource().sendFailure(new StringTextComponent("You need to specify a position!"));
                    return 0;
                })
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(context -> {
                            context.getSource().sendFailure(new StringTextComponent("You need to specify an action!"));
                            return 0;
                        })
                        .then(Commands.argument("action", StringArgumentType.word())
                                .executes(context -> {

                                    try{
                                        if(!PermissionAPI.hasPermission(context.getSource().getPlayerOrException(), "pixelmonaddons.command.random-legendary-uc")) {
                                            StringTextComponent message = new StringTextComponent("You do not have access to this command!");
                                            message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                            context.getSource().sendFailure(message);
                                            return 0;
                                        }
                                    } catch (Exception ignored){

                                    }

                                    LocationInput pos = context.getArgument("pos", LocationInput.class);

                                    Species specie;

                                    do{
                                        specie = PixelmonSpecies.getRandomLegendary(false);
                                    } while (!isSpecieValid(specie));

                                    Pokemon pokemon = PokemonBuilder.builder().species(specie.getDex()).shiny(false).build();

                                    PixelmonEntity pokeEntity = pokemon.getOrCreatePixelmon(
                                            context.getSource().getLevel(),
                                            pos.getPosition(context.getSource()).x,
                                            pos.getPosition(context.getSource()).y,
                                            pos.getPosition(context.getSource()).z
                                    );
                                    pokeEntity.canDespawn = false;
                                    pokeEntity.setSpawnLocation(pokeEntity.getDefaultSpawnLocation());
                                    context.getSource().getLevel().addFreshEntity(pokeEntity);
                                    pokeEntity.resetAI();

                                    return 1;
                                })
                                .then(Commands.argument("shiny", StringArgumentType.word())
                                        .executes(context -> {

                                            try{
                                                if(!PermissionAPI.hasPermission(context.getSource().getPlayerOrException(), "pixelmonaddons.command.random-legendary-uc")) {
                                                    StringTextComponent message = new StringTextComponent("You do not have access to this command!");
                                                    message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                                    context.getSource().sendFailure(message);
                                                    return 0;
                                                }
                                            } catch (Exception ignored){

                                            }

                                            if(!context.getArgument("shiny", String.class).equalsIgnoreCase("shiny")) {
                                                context.getSource().sendFailure(new StringTextComponent("Invalid option! You should use shiny!"));
                                                return 0;
                                            }

                                            LocationInput pos = context.getArgument("pos", LocationInput.class);

                                            Species specie;

                                            do{
                                                specie = PixelmonSpecies.getRandomLegendary(false);
                                            } while (!isSpecieValid(specie));

                                            Pokemon pokemon = PokemonBuilder.builder().species(specie.getDex()).shiny().build();

                                            PixelmonEntity pokeEntity = pokemon.getOrCreatePixelmon(
                                                    context.getSource().getLevel(),
                                                    pos.getPosition(context.getSource()).x,
                                                    pos.getPosition(context.getSource()).y,
                                                    pos.getPosition(context.getSource()).z
                                            );
                                            pokeEntity.canDespawn = false;
                                            pokeEntity.setSpawnLocation(pokeEntity.getDefaultSpawnLocation());
                                            context.getSource().getLevel().addFreshEntity(pokeEntity);
                                            pokeEntity.resetAI();

                                            return 1;
                                        })
                                )
                        )
                )

        );

    }

    private static boolean isSpecieValid(Species species) {

        return !PixelmonAddons.getConfig().getBlacklistedLegendaries().contains(species.getName());

    }

}
