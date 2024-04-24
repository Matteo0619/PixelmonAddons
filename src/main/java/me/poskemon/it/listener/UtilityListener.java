package me.poskemon.it.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.blocks.MysteryBoxEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UtilityListener {

    public UtilityListener() {
        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onMysteryBoxSpawnMeltan(MysteryBoxEvent.Tick.DoSpawn event) {

        World world = event.getBox().getLevel();

        if(world == null) {
            return;
        }

        BlockPos pos = event.getBox().getBlockPos();

        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

    }

}
