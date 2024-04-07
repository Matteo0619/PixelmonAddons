package me.poskemon.it.config;


import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import info.pixelmon.repack.org.spongepowered.objectmapping.meta.Comment;

import java.util.List;

@ConfigSerializable
@ConfigPath("config/PixelmonAddons/commands.yml")
public class CommandsConfig extends AbstractYamlConfig {

    public CommandsConfig() {
        super();
    }

    @Comment("Legendaries to be removed from random Legendary list")
    private List<String> blacklistedLegendaries = Lists.newArrayList("Wo-Chien", "Ting-Lu", "Koraidon", "Miraidon", "Okidogi", "Munkidori", "Fezandipiti", "Ogerpon");

    @Comment("Legendaries to be added to random Legendary list")
    private List<String> addedLegendaries = Lists.newArrayList("IronLeaves", "WalkingWake");

    @Comment("Random Legendary spawning level")
    private int randomLegendaryLevel = 70;

    @Comment("Random UltraBeast spawning level")
    private int randomUltraBeastLevel = 70;

    @Comment("Time in second")
    private int breedCooldown = 1800;

    @Comment("Time in second")
    private int hatchCooldown = 1800;

    public List<String> getBlacklistedLegendaries() {
        return blacklistedLegendaries;
    }

    public List<String> getAddedLegendaries() {
        return addedLegendaries;
    }

    public int getRandomLegendaryLevel() {
        return randomLegendaryLevel;
    }

    public int getRandomUltraBeastLevel() {
        return randomUltraBeastLevel;
    }

    public int getBreedCooldown() {
        return breedCooldown;
    }

    public int getHatchCooldown() {
        return hatchCooldown;
    }

}
