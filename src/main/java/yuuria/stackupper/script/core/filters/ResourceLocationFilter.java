package yuuria.stackupper.script.core.filters;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yuuria.stackupper.StackUpperHelpers;
import yuuria.stackupper.script.core.FilterBase;

import java.util.List;

public class ResourceLocationFilter extends FilterBase {
    private List<Integer> meta;

    public List<Integer> getMeta() { return meta; }

    @Override
    public boolean test(Item item, ItemStack stack) {
        ResourceLocation resourceLocation = item.getRegistryName();
        if (resourceLocation == null) return false;

        boolean nameMatches;
        if (Boolean.TRUE.equals(regex)) {
            nameMatches = resourceLocation.toString().matches(StackUpperHelpers.globToRegex(value));
        } else {
            nameMatches = resourceLocation.toString().equals(value);
        }
        if (!nameMatches) return false;

        if (meta == null || meta.isEmpty()) return true;
        int itemMeta = stack != null ? stack.getMetadata() : 0;
        return meta.contains(itemMeta);
    }
}
