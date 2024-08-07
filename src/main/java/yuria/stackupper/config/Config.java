package yuria.stackupper.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import yuria.stackupper.StackUpper;

@EventBusSubscriber(modid = StackUpper.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue stackSize = BUILDER
            .comment("idk lol")
            .defineInRange("stackSize", 64, 64, StackSize.MAX_ALLOWED);

    public static ModConfigSpec SPEC = BUILDER.build();

    public static int stack;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        stack = stackSize.get();
    }
}
