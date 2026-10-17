package yuuria.stackupper.script.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class FilterBase {
    protected String type;
    protected String value;
    protected Boolean regex;

    public abstract boolean test(Item item, ItemStack stack);
    public String getValue() { return value; }
    public String getType() { return type; }
    public Boolean getRegex() { return regex; }
}
