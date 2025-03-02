package yuuria.stackupper.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import yuuria.stackupper.coremod.transformer.SlotLimit;

import java.util.List;

public class SUCoreMod implements ICoreMod {

    @Override
    public Iterable<? extends ITransformer<?>> getTransformers() {
        return List.of(
            new SlotLimit()
        );
    }
}
