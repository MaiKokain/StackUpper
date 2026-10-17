package yuuria.stackupper.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import yuuria.stackupper.StackUpper;
import yuuria.stackupper.StackUpperHelpers;
import yuuria.stackupper.stackupper.Tags;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class SyncItemEvents {
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP && !event.player.world.isRemote) {
            StackUpper.LOGGER.info("Syncing data to {{}={}}", event.player.getName(), event.player.getUniqueID());
            sendTo((EntityPlayerMP) event.player);
        }
    }

    private static void sendTo(EntityPlayerMP player) {
        StackUpper.INSTANCE.sendTo(new SyncItemData(StackUpperHelpers.appliedStackMap), player);
    }
}
