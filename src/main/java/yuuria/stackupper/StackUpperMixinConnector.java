package yuuria.stackupper;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;
import yuuria.stackupper.stackupper.Tags;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

public class StackUpperMixinConnector implements IMixinConnector, ILateMixinLoader {
    @Override
    public void connect()
    {
        Mixins.addConfiguration("stackupper.mixins.json");
    }

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("stackupper.mixins.json");
    }
}
