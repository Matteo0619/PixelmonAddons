package me.poskemon.it.config;

import com.pixelmonmod.pixelmon.api.config.api.data.ConfigPath;
import com.pixelmonmod.pixelmon.api.config.api.yaml.AbstractYamlConfig;
import info.pixelmon.repack.org.spongepowered.objectmapping.ConfigSerializable;
import info.pixelmon.repack.org.spongepowered.objectmapping.meta.Comment;

@ConfigSerializable
@ConfigPath("config/PixelmonAddons/messages.yml")
public class MessageConfig extends AbstractYamlConfig {

    public MessageConfig() {
        super();
    }

    @Comment("Sent if no slot where specified")
    private String changeGenderNoSlot = "§cYou need to specify a slot!";

    @Comment("Sent if command is not used by a player")
    private String changeGenderInvalidSource = "§cThis is a player only command!";

    @Comment("Sent if the user doesn't have the permission")
    private String changeGenderNoPermission = "§cYou do not have access to this command!";

    @Comment("Sent if the slot specified was not a number")
    private String changeGenderInvalidNumber = "§cInvalid number";

    @Comment("Sent if the slot specified was not valid")
    private String changeGenderInvalidSlot = "§cInvalid slot";

    @Comment("Sent if the slot specified was empty")
    private String changeGenderEmptySlot = "§che input slot is empty";

    @Comment("Sent if target pokemon cannot have is gender changed")
    private String changeGenderInvalidPokemon = "§cYou cannot change gender to this pokemon";

    @Comment("Sent if command is used successfully")
    private String changeGenderSuccess = "§aGender correctly changed";

    public String getChangeGenderNoSlot() {
        return changeGenderNoSlot;
    }

    public String getChangeGenderInvalidSource() {
        return changeGenderInvalidSource;
    }

    public String getChangeGenderNoPermission() {
        return changeGenderNoPermission;
    }

    public String getChangeGenderInvalidNumber() {
        return changeGenderInvalidNumber;
    }

    public String getChangeGenderInvalidSlot() {
        return changeGenderInvalidSlot;
    }

    public String getChangeGenderEmptySlot() {
        return changeGenderEmptySlot;
    }

    public String getChangeGenderInvalidPokemon() {
        return changeGenderInvalidPokemon;
    }

    public String getChangeGenderSuccess() {
        return changeGenderSuccess;
    }
}
