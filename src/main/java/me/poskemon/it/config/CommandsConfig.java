package me.poskemon.it.config;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
@ConfigPath("config/PixelmonAddons/commands.yml")
public class CommandsConfig extends AbstractYamlConfig {

    public CommandsConfig() {
        super();
    }

    private List<String> blacklistedLegendaries = Lists.newArrayList("Wo-Chien", "Ting-Lu", "Koraidon", "Miraidon", "Okidogi", "Munkidori", "Fezandipiti", "Ogerpon");

    private List<String> addedLegendaries = Lists.newArrayList("IronLeaves", "WalkingWake");

    public List<String> getBlacklistedLegendaries() {
        return blacklistedLegendaries;
    }

    public List<String> getAddedLegendaries() {
        return addedLegendaries;
    }
}
