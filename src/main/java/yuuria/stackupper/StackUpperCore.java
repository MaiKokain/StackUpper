package yuuria.stackupper;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import yuuria.stackupper.stackupper.Tags;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.*;

@IFMLLoadingPlugin.Name("StackUpperCore")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({
        "yuuria.stackupper.core",
})
public class StackUpperCore implements IEarlyMixinLoader, IFMLLoadingPlugin {
    @Override
    public @Nullable String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public @Nullable String getModContainerClass() {
        return null;
    }

    @Override
    public @Nullable String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public @Nullable String getAccessTransformerClass() {
        return "yuuria.stackupper.core.StackUpperTransformer";
    }

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("stackupper.early.mixins.json");
    }

}
