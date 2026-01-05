package yuuria.stackupper.stackupper;

import net.minecraft.core.registries.BuiltInRegistries;
import yuuria.stackupper.configlibrary.Constant;

public final class SpecialSetter {
    static {
        Constant.setBuiltinRegistriesGetter(resourceLocation -> BuiltInRegistries.ITEM.get(resourceLocation).get().value());
    }
}
