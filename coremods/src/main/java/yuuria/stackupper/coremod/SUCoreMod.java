package yuuria.stackupper.coremod;

import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.api.ITransformer;
import net.neoforged.neoforgespi.coremod.ICoreMod;
import org.slf4j.Logger;
import yuuria.stackupper.coremod.transformer.Replace64WithInteger;
import yuuria.stackupper.coremod.transformer.SlotLimit;

import java.util.List;

public class SUCoreMod implements ICoreMod {
    public static Logger logger = LogUtils.getLogger();

    @Override
    public Iterable<? extends ITransformer<?>> getTransformers() {
        return List.of(
            new SlotLimit(),
            new Replace64WithInteger()
        );
    }
}
