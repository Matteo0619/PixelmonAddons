package me.poskemon.it;


import com.pixelmonmod.pixelmon.api.config.api.yaml.YamlConfigFactory;
import me.poskemon.it.command.*;
import me.poskemon.it.config.CommandsConfig;
import me.poskemon.it.config.MessageConfig;
import me.poskemon.it.listener.LastLegendaryListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(PixelmonAddons.MOD_ID)
@Mod.EventBusSubscriber(modid = PixelmonAddons.MOD_ID)
public class PixelmonAddons {

    public static final String MOD_ID = "pixelmonaddons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static PixelmonAddons instance;

    private CommandsConfig commandsConfig;

    private MessageConfig messageConfig;

    private static Map<UUID, LastLegendaryData> lastLegendaries = new HashMap<>();

    private static Map<UUID, LocalDateTime> breedCooldown = new HashMap<>();

    private static Map<UUID, LocalDateTime> hatchCooldown = new HashMap<>();

    public PixelmonAddons() {
        instance = this;

        reloadConfig();

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(PixelmonAddons::onModLoad);
    }

    public static void onModLoad(FMLCommonSetupEvent event) {


    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        new LastLegendaryListener();
    }

    public void reloadConfig() {
        try {
            commandsConfig = YamlConfigFactory.getInstance(CommandsConfig.class);
            messageConfig = YamlConfigFactory.getInstance(MessageConfig.class);
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
    }

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {

    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {

        ResetEvsCommand.register(event.getDispatcher());
        ChangeGenderCommand.register(event.getDispatcher());
        PokemonTypeCommand.register(event.getDispatcher());
        SpawnRandomLegendaryCommand.register(event.getDispatcher());
        SpawnRandomUltraBeastCommand.register(event.getDispatcher());
        LastLegendaryCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onServerStopping(FMLServerStoppingEvent event) {

    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {


    }

    public static PixelmonAddons getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static CommandsConfig getCommandConfig() {
        return instance.commandsConfig;
    }

    public static MessageConfig getMessageConfig() { return  instance.messageConfig; }

    public static Map<UUID, LastLegendaryData> getLastLegendaries() {
        return lastLegendaries;
    }

    public static Map<UUID, LocalDateTime> getBreedCooldown() {
        return breedCooldown;
    }

    public static Map<UUID, LocalDateTime> getHatchCooldown() {
        return hatchCooldown;
    }
}

