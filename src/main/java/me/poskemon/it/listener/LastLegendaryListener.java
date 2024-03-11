package me.poskemon.it.listener;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.poskemon.it.LastLegendaryData;
import me.poskemon.it.LastLegendaryStatus;
import me.poskemon.it.PixelmonAddons;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class LastLegendaryListener {

    public LastLegendaryListener() {
        Pixelmon.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLegendarySpawn(SpawnEvent event) {

        Entity entity = event.action.getOrCreateEntity();

        if(!(entity instanceof PixelmonEntity)) {
            return;
        }

        PixelmonEntity pixelmonEntity = (PixelmonEntity) entity;

        Species species = pixelmonEntity.getSpecies();

        if(PixelmonSpecies.getLegendaries(false).contains(species.getDex())) {

            LastLegendaryData data = new LastLegendaryData(species, LastLegendaryStatus.ALIVE, ZonedDateTime.now());
            PixelmonAddons.getLastLegendaries().put(pixelmonEntity.getUUID(), data);

        }

    }

    @SubscribeEvent
    public void onLegendaryCapture(CaptureEvent event) {

        PixelmonEntity entity = event.getPokemon();

        UUID id = entity.getUUID();

        Map<UUID, LastLegendaryData> lastLegendaries = PixelmonAddons.getLastLegendaries();

        if(lastLegendaries.containsKey(id)) {

            LastLegendaryData data = lastLegendaries.get(id);
            data.setStatus(LastLegendaryStatus.CAPTURED);

        }

    }

    @SubscribeEvent
    public void onLegendaryDefeat(BeatWildPixelmonEvent event) {

        PixelmonEntity entity = event.wpp.asWrapper().entity;

        UUID id = entity.getUUID();

        Map<UUID, LastLegendaryData> lastLegendaries = PixelmonAddons.getLastLegendaries();

        if(lastLegendaries.containsKey(id)) {

            LastLegendaryData data = lastLegendaries.get(id);
            data.setStatus(LastLegendaryStatus.DEFEATED);

        }

    }

    @SubscribeEvent
    public void onLegendaryDespawn(EntityLeaveWorldEvent event) {

        Entity entity = event.getEntity();

        if(!(entity instanceof PixelmonEntity)) {
            return;
        }

        PixelmonEntity pixelmonEntity = (PixelmonEntity) entity;

        UUID id = entity.getUUID();

        Map<UUID, LastLegendaryData> lastLegendaries = PixelmonAddons.getLastLegendaries();

        if(lastLegendaries.containsKey(id)) {

            LastLegendaryData data = lastLegendaries.get(id);
            data.setStatus(LastLegendaryStatus.DESPAWNED);

        }

    }

}
