package yuria.stackupper;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import yuria.stackupper.events.Client;
import yuria.sul.ast.core.Processor;
import yuria.sul.ast.item.ItemCollection;

import java.io.File;

@Mod(StackUpper.MODID)
public class StackUpper
{
    public static final String MODID = "stackupper";
    public static final ItemCollection itemCollection = new ItemCollection();
    public static final File StackUpperLangFolder = new File(FMLPaths.CONFIGDIR.get().toFile().getAbsolutePath(), "stackupper");
    public static final Processor astProccessor = new Processor();
    public static final Logger LOGGER = LogUtils.getLogger();

    public StackUpper(ModContainer modContainer) {
        if (FMLEnvironment.dist.isClient()) NeoForge.EVENT_BUS.register(Client.class);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, StackUpperConfig.CONFIG_SPEC);

        this.checkAndGenerateConfig();
    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event)
    {
        this.checkAndGenerateConfig();

        if (StackUpperConfig.CONFIG.enableScripting.get()) {
            astProccessor.processFileToArray(StackUpperLangFolder);
            StackUpper.astProccessor.start();
        }
    }

    private void checkAndGenerateConfig()
    {
        if (!StackUpper.StackUpperLangFolder.exists()) {
            try {
                StackUpper.StackUpperLangFolder.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}