package me.poskemon.it.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.Element;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.*;
import java.util.stream.Collectors;

public class PokemonTypeCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        Map<Float, Set<StringTextComponent>> map = new HashMap<>();

        map.put(4.0F, new HashSet<>());
        map.put(2.0F, new HashSet<>());
        map.put(0.5F, new HashSet<>());
        map.put(0.25F, new HashSet<>());
        map.put(0.0F, new HashSet<>());

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("type")
                .executes(context -> {
                    StringTextComponent message = new StringTextComponent("You need to specify a pokemon!");
                    message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                    context.getSource().sendSuccess(message, false);
                    return 0;
                })
                .then(Commands.argument("pokemon", StringArgumentType.greedyString())
                        .suggests((a, b) -> {
                            return ISuggestionProvider.suggest(PixelmonSpecies.getAll().stream().map(Species::getName), b);})
                        .executes(context -> {

                            if(!PermissionAPI.hasPermission(context.getSource().getPlayerOrException(), "pixelmonaddons.command.type")) {
                                StringTextComponent message = new StringTextComponent("You do not have access to this command!");
                                message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                context.getSource().sendFailure(message);
                                return 0;
                            }

                            String pokemonName = context.getArgument("pokemon", String.class);

                            Optional<RegistryValue<Species>> optional = PixelmonSpecies.get(pokemonName);

                            if(!optional.isPresent()) {
                                StringTextComponent message = new StringTextComponent("Invalid pokemon input!");
                                message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                context.getSource().sendSuccess(message, false);
                                return 0;
                            }

                            Species pokemon = optional.get().getValueUnsafe();

                            List<Element> elements = pokemon.getDefaultForm().getTypes();

                            List<Element> all = Element.getElements().stream().filter(e -> !Element.MYSTERY.equals(e)).collect(Collectors.toList());

                            for (Element e : all) {
                                Float effect = Element.getTotalEffectiveness(elements, e);

                                if(map.containsKey(effect)) {
                                    StringTextComponent type = new StringTextComponent(e.getName());
                                    type.setStyle(type.getStyle().withColor(Color.fromRgb(e.getColor())));
                                    map.get(effect).add(type);
                                }
                            }

                            int dex = pokemon.getDex();

                            buildAndSendMessage(context, dex, pokemon, elements, map);

                            return 1;
                        })
                )

        );


    }

    private static void buildAndSendMessage(CommandContext<CommandSource> context, int dex, Species pokemon, List<Element> elements, Map<Float, Set<StringTextComponent>> map) {
        StringTextComponent dexNumber = new StringTextComponent("#" + dex);
        dexNumber.setStyle(dexNumber.getStyle().withColor(Color.fromLegacyFormat(TextFormatting.BLUE)));
        StringTextComponent pokeName = new StringTextComponent(pokemon.getName());
        pokeName.setStyle(pokeName.getStyle().withColor(Color.fromLegacyFormat(TextFormatting.WHITE)));

        StringTextComponent types = new StringTextComponent("");
        for(Element e : elements) {
            StringTextComponent type = new StringTextComponent(e.getName());
            type.setStyle(type.getStyle().withColor(Color.fromRgb(e.getColor())));
            types.append(" ").append(type);
        }

        IFormattableTextComponent line = new StringTextComponent("(").append(dexNumber).append(") ").append(pokeName)
                        .append(" (").append(types).append(" )");

        context.getSource().sendSuccess(line, false);

        if(!map.get(4.0F).isEmpty()) {
            StringTextComponent message = new StringTextComponent("x4: ");
            message.setStyle(message.getStyle().applyFormat(TextFormatting.AQUA));
            for(StringTextComponent t : map.get(4.0F)) {
                message.append(t).append(" ");
            }
            context.getSource().sendSuccess(message, false);
        }

        if(!map.get(2.0F).isEmpty()) {
            StringTextComponent message = new StringTextComponent("x2: ");
            message.setStyle(message.getStyle().applyFormat(TextFormatting.GREEN));
            for(StringTextComponent t : map.get(2.0F)) {
                message.append(t).append(" ");
            }
            context.getSource().sendSuccess(message, false);
        }

        if(!map.get(0.5F).isEmpty()) {
            StringTextComponent message = new StringTextComponent("x0.5: ");
            message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
            for(StringTextComponent t : map.get(0.5F)) {
                message.append(t).append(" ");
            }
            context.getSource().sendSuccess(message, false);
        }

        if(!map.get(0.25F).isEmpty()) {
            StringTextComponent message = new StringTextComponent("x0.25: ");
            message.setStyle(message.getStyle().applyFormat(TextFormatting.DARK_PURPLE));
            for(StringTextComponent t : map.get(0.25F)) {
                message.append(t).append(" ");
            }
            context.getSource().sendSuccess(message, false);
        }

        if(!map.get(0.0F).isEmpty()) {
            StringTextComponent message = new StringTextComponent("x0: ");
            message.setStyle(message.getStyle().applyFormat(TextFormatting.DARK_GRAY));
            for(StringTextComponent t : map.get(0.0F)) {
                message.append(t).append(" ");
            }
            context.getSource().sendSuccess(message, false);
        }
    }

}
