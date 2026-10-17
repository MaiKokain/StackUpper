package yuuria.stackupper.script.core.filters;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import yuuria.stackupper.StackUpperHelpers;
import yuuria.stackupper.script.core.FilterBase;

public class OreDictFilter extends FilterBase {

    @Override
    public boolean test(Item item, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        int[] ids = OreDictionary.getOreIDs(stack);
        if (ids == null || ids.length == 0) return false;

        for (int id : ids) {
            String name = OreDictionary.getOreName(id);
            if (Boolean.TRUE.equals(regex) ? name.matches(StackUpperHelpers.globToRegex(value)) : name.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
