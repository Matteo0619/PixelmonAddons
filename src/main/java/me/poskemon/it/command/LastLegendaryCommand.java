package me.poskemon.it.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.poskemon.it.LastLegendaryData;
import me.poskemon.it.PixelmonAddons;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.permission.PermissionAPI;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LastLegendaryCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("lastlegendary")
                .executes(context -> {
                    ServerPlayerEntity player;

                    try {
                        player = context.getSource().getPlayerOrException();
                    } catch (Exception e) {
                        context.getSource().sendFailure(new StringTextComponent("This is a player only command!"));
                        return 0;
                    }

//                    if (!PermissionAPI.hasPermission(player, "pixelmonaddons.command.lastlegendary")) {
//                        StringTextComponent message = new StringTextComponent("You do not have access to this command!");
//                        message.setStyle(message.getStyle().applyFormat(TextFormatting.RED));
//                        context.getSource().sendFailure(message);
//                        return 0;
//                    }

                    List<UUID> uuids = new ArrayList<>(PixelmonAddons.getLastLegendaries().keySet());

                    Collections.reverse(uuids);

                    uuids = uuids.stream().limit(3).collect(Collectors.toList());

                    buildAndSendMessage(context, uuids);

                    return 1;
                })

        );
    }

    private static void buildAndSendMessage(CommandContext<CommandSource> context, List<UUID> uuids) {

        StringTextComponent title = new StringTextComponent("Last Legendary spawned: ");
        title.setStyle(title.getStyle().withColor(Color.fromLegacyFormat(TextFormatting.AQUA)));

        IFormattableTextComponent message = new StringTextComponent("").append(title).append("\n");

        Map<UUID, LastLegendaryData> lastLegendaries = PixelmonAddons.getLastLegendaries();

        for(UUID id : uuids) {

            LastLegendaryData data = lastLegendaries.get(id);

            StringTextComponent name = new StringTextComponent(data.getSpecies().getName());
            name.setStyle(name.getStyle().withColor(TextFormatting.GRAY));

            StringTextComponent time = new StringTextComponent(data.getSpawnTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            time.setStyle(time.getStyle().withColor(TextFormatting.GRAY));

            StringTextComponent status = new StringTextComponent(data.getStatus().toString());
            status.setStyle(status.getStyle().withColor(data.getStatus().getColor()));

            message.append("\nPokemon: ").append(name).append(" Time: ").append(time).append( " Status: ").append(status).append("\n");

        }

        context.getSource().sendSuccess(message, false);

    }

}
