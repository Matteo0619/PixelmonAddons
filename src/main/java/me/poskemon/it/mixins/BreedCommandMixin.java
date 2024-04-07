package me.poskemon.it.mixins;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.command.PixelCommand;
import com.pixelmonmod.pixelmon.command.impl.BreedCommand;
import me.poskemon.it.PixelmonAddons;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.server.permission.PermissionAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Mixin(BreedCommand.class)
public abstract class BreedCommandMixin extends PixelCommand {

    public BreedCommandMixin(CommandDispatcher<CommandSource> dispatcher, String name, String usage, int permissionLevel) {
        super(dispatcher, name, usage, permissionLevel);
    }

    public BreedCommandMixin(CommandDispatcher<CommandSource> dispatcher) {
        super(dispatcher);
    }

    @Inject(method = "execute", at = @At("HEAD"), remap = false)
    private void injectHead(CommandSource sender, String[] args, CallbackInfo ci) throws CommandSyntaxException {

        args = PixelmonCommandUtils.setupCommandTargets(this, sender, args, 0);

        Map<UUID, LocalDateTime> map = PixelmonAddons.getBreedCooldown();

        ServerPlayerEntity player = PixelmonCommandUtils.requireEntityPlayer(args[0]);

        if(PermissionAPI.hasPermission(player, "pixelmonaddons.cooldown.breed-bypass")) {
            return;
        }

        if(!map.containsKey(player.getUUID())) {
            return;
        }

        LocalDateTime prev = map.get(player.getUUID());
        LocalDateTime now = LocalDateTime.now();

        long breedCooldown = PixelmonAddons.getCommandConfig().getBreedCooldown();

        long diff = Duration.between(prev, now).getSeconds();

        if(diff > breedCooldown) {
            return;
        }

        long duration = Duration.between(prev, now).getSeconds();

        long minutes = (breedCooldown - duration) / 60;

        long seconds = (breedCooldown - duration) % 60;

        IFormattableTextComponent message = new StringTextComponent("§aBreed §fcommand is in cooldown. Wait ");

        message = minutes != 0 ? message.append(minutes + "§amin §f").append(seconds + "§asec") : message.append(seconds + "§asec");

        player.sendMessage(message, UUID.randomUUID());

        ci.cancel();

    }

    @Inject(method = "execute", at = @At("TAIL"), remap = false)
    private void injectTail(CommandSource sender, String[] args, CallbackInfo ci) {

        Map<UUID, LocalDateTime> map = PixelmonAddons.getBreedCooldown();

        ServerPlayerEntity player = PixelmonCommandUtils.requireEntityPlayer(args[0]);

        if(PermissionAPI.hasPermission(player, "pixelmonaddons.cooldown.breed-bypass")) {
            return;
        }

        map.put(player.getUUID(), LocalDateTime.now());

    }

}
