package me.poskemon.it.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;

@ConfigSerializable
@ConfigPath("config/PixelmonAddons/messages.yml")
public class ExampleConfig extends AbstractYamlConfig {

    private String exampleField = "Hello World";

    public ExampleConfig() {
        super();
    }

    public String getExampleField() {
        return this.exampleField;
    }
}
