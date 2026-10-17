package yuuria.stackupper.script.core.filters;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yuuria.stackupper.StackUpperHelpers;
import yuuria.stackupper.script.core.FilterBase;

public class ModFilter extends FilterBase {

    @Override
    public boolean test(Item item, ItemStack stack) {
        ResourceLocation key = item.getRegistryName();
        if (key == null) return false;
        String modid = key.getNamespace();
        return Boolean.TRUE.equals(regex) ? modid.matches(StackUpperHelpers.globToRegex(value)) : modid.equals(value);
    }
}