package yuuria.stackupper;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yuuria.stackupper.stackupper.Tags;
import yuuria.stackupper.commands.DebugCmds;
import yuuria.stackupper.commands.StackUpperCommand;
import yuuria.stackupper.network.SyncItemData;
import yuuria.stackupper.proxy.StackUpperCommonProxy;
import yuuria.stackupper.script.ScriptHandler;

import java.io.File;

import static yuuria.stackupper.StackUpperHelpers.STACKUP_FOLDER;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class StackUpper {
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static StackUpperConfig CONFIG;

    @SidedProxy(modId = Tags.MOD_ID, clientSide = "yuuria.stackupper.proxy.StackUpperClientProxy", serverSide = "yuuria.stackupper.proxy.StackUpperCommonProxy")
    public static StackUpperCommonProxy proxy;

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new StackUpperCommand());
        if (event.getServer().isDedicatedServer()) {
            StackUpperHelpers.trueConf = StackUpperHelpers.generateConfigInt();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (event.getSide().isClient()) {
            ClientCommandHandler.instance.registerCommand(new DebugCmds());
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG = StackUpperConfig.init(event.getModConfigurationDirectory().toPath());

        STACKUP_FOLDER = new File(event.getModConfigurationDirectory(), "stackupper");
        if (!STACKUP_FOLDER.exists()) {
            STACKUP_FOLDER.mkdir();
        }

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        INSTANCE.registerMessage(SyncItemData.Handler.class, SyncItemData.class, 0, Side.CLIENT);
        Items.AIR.setMaxStackSize(64);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        process(ForgeRegistries.ITEMS);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRegisterItem(RegistryEvent.Register<Item> event)
    {
        process(event.getRegistry());
    }

    public static void process(IForgeRegistry<Item> items)
    {
        if (!StackUpperHelpers.appliedStackMap.isEmpty()) StackUpperHelpers.appliedStackMap.clear();
        new ScriptHandler().process(items, STACKUP_FOLDER);
    }
}
