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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

import java.util.*;
import java.util.stream.Collectors;

public class PokemonTypeCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("type")
                .executes(context -> {
                    StringTextComponent message = new StringTextComponent("You need to specify a pokemon!");
                    message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                    context.getSource().sendSuccess(message, false);
                    return 0;
                })
                .then(Commands.argument("pokemon", StringArgumentType.word())
                        .suggests((a, b) -> {
                            return ISuggestionProvider.suggest(PixelmonSpecies.getAll().stream().map(Species::getName), b);})
                        .executes(context -> {

                            ServerPlayerEntity player;

                            try {
                                player = context.getSource().getPlayerOrException();
                            } catch (Exception e) {
                                context.getSource().sendFailure(new StringTextComponent("This is a player only command!"));
                                return 0;
                            }

                            if(!PermissionAPI.hasPermission(player, "pixelmonaddons.command.type")) {
                                StringTextComponent message = new StringTextComponent("You do not have access to this command!");
                                message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                context.getSource().sendFailure(message);
                                return 0;
                            }

                            String[] arr = context.getArgument("pokemon", String.class).split("_");

                            String pokemonName = arr[0];

                            String form = "";
                            if(arr.length > 1) {
                                form = getCorrectForm(arr[1]);
                            }

                            Optional<RegistryValue<Species>> optional = PixelmonSpecies.get(pokemonName);

                            if(!optional.isPresent()) {
                                StringTextComponent message = new StringTextComponent("Invalid pokemon input!");
                                message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
                                context.getSource().sendSuccess(message, false);
                                return 0;
                            }

                            Map<Float, Set<StringTextComponent>> map = new HashMap<>();

                            map.put(4.0F, new HashSet<>());
                            map.put(2.0F, new HashSet<>());
                            map.put(0.5F, new HashSet<>());
                            map.put(0.25F, new HashSet<>());
                            map.put(0.0F, new HashSet<>());

                            Species pokemon = optional.get().getValueUnsafe();

                            List<Element> elements = pokemon.getForm(form).getTypes();

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

        buildWeaknessMessage(map, 4.0F, "x4: ", TextFormatting.AQUA, context);

        buildWeaknessMessage(map, 2.0F, "x2: ", TextFormatting.GREEN, context);

        buildWeaknessMessage(map, 0.5F, "x0.5: ", TextFormatting.RED, context);

        buildWeaknessMessage(map, 0.25F, "x0.25: ", TextFormatting.DARK_PURPLE, context);

        buildWeaknessMessage(map, 0.0F, "x0: ", TextFormatting.DARK_GRAY, context);
    }

    private static void buildWeaknessMessage(Map<Float, Set<StringTextComponent>> map, float key, String p_i45159_1_, TextFormatting aqua, CommandContext<CommandSource> context) {
        if (!map.get(key).isEmpty()) {
            StringTextComponent message = new StringTextComponent(p_i45159_1_);
            message.setStyle(message.getStyle().applyFormat(aqua));
            for (StringTextComponent t : map.get(key)) {
                message.append(t).append(" ");
            }
            context.getSource().sendSuccess(message, false);
        }
    }

    private static String getCorrectForm(String form) {

        switch (form.toLowerCase()) {
            case "hisui" :
            case "hisuian" :
                return "hisuian";

            case "galar" :
            case "galarian" :
                return "galarian";

            case "alola" :
            case "alolan" :
                return "alolan";

            default:
                return "";
        }
    }

}
