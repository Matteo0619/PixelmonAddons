package me.poskemon.it;

import com.pixelmonmod.pixelmon.api.pokemon.species.Species;

import java.time.ZonedDateTime;

public class LastLegendaryData {

    private Species species;

    private LastLegendaryStatus status;

    private ZonedDateTime spawnTime;

    public LastLegendaryData(Species species, LastLegendaryStatus status, ZonedDateTime spawnTime) {
        this.species = species;
        this.status = status;
        this.spawnTime = spawnTime;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public LastLegendaryStatus getStatus() {
        return status;
    }

    public void setStatus(LastLegendaryStatus status) {
        this.status = status;
    }

    public ZonedDateTime getSpawnTime() {
        return spawnTime;
    }

    public void setSpawnTime(ZonedDateTime spawnTime) {
        this.spawnTime = spawnTime;
    }
}
