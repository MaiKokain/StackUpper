package yuria.stackupper;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import yuria.stackupper.events.Client;

@Mod(Constants.MODID)
public class StackUpper
{
    public static final Logger LOGGER = LogUtils.getLogger();

    public StackUpper() {
        if (FMLEnvironment.dist.isClient()) NeoForge.EVENT_BUS.register(Client.class);
        NeoForge.EVENT_BUS.register(this);

        if (!Constants.StackUpperLangFolder.exists()) Constants.StackUpperLangFolder.mkdir();

    }

    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event)
    {
        Constants.astProccessor.start();
    }
}