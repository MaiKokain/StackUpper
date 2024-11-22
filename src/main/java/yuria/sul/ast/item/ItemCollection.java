package yuria.sul.ast.item;


import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

import java.util.*;

public class ItemCollection  {
    public final Map<Item, ItemProperty> cache = new LinkedHashMap<>();

    public void put(ItemProperty itemProperty)
    {
        cache.put(itemProperty.item, itemProperty);
    }

    public ItemProperty get(Item item)
    {
        return cache.get(item);
    }

    public Boolean contains(Item item)
    {
        return cache.containsKey(item);
    }

    public void remove(Item item) {
        cache.remove(item);
    }

    public void clear() {
        cache.clear();
    }

    public boolean matchValue(Item item, ItemProperty property)
    {
        return this.get(item).equals(property);
    }
}
