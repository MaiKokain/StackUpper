package yuria.stackupper;

import com.mojang.logging.LogUtils;
import eu.midnightdust.lib.config.MidnightConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import yuria.stackupper.config.StackUpperConfig;
import yuria.stackupper.events.Client;

@Mod(StackUpper.MODID)
public class StackUpper
{
    public static final String MODID = "stackupper";
    public static final Logger LOGGER = LogUtils.getLogger();

    public StackUpper(ModContainer container) {
        LOGGER.info("StackUpper!");

        if (FMLEnvironment.dist.isClient()) NeoForge.EVENT_BUS.register(Client.class);
        MidnightConfig.init("yuria.stackupper", StackUpperConfig.class);
        container.registerExtensionPoint(IConfigScreenFactory.class, ((minecraft, modListScreen) -> MidnightConfig.getScreen(modListScreen, "yuria.stackupper")));
    }
}
