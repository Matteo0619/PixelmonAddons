package me.poskemon.it;

import me.poskemon.it.command.LastLegendaryCommand;
import net.minecraft.util.text.TextFormatting;

public enum LastLegendaryStatus {

    ALIVE(TextFormatting.GREEN),
    DEFEATED(TextFormatting.RED),
    DESPAWNED(TextFormatting.YELLOW),
    CAPTURED(TextFormatting.GOLD);

    TextFormatting color;

    LastLegendaryStatus(TextFormatting color) {
        this.color = color;
    }

    public TextFormatting getColor() {
        return color;
    }
}
