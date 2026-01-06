package yuuria.stackupper.coremod;

import net.neoforged.neoforgespi.transformation.ClassProcessorProvider;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import yuuria.stackupper.coremod.SUL.SlotLimitFix;

import java.util.List;

public class SUCoreMod implements ClassProcessorProvider {
    @Override
    public void createProcessors(Context context, Collector collector) {
        collector.add(
                new SlotLimitFix(List.of(
                        new SimpleMethodProcessor.Target("net.minecraft.world.Container", "getMaxStackSize", "()I")
                ))
        );
    }
}
