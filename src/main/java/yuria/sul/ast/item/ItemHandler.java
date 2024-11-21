package yuria.sul.ast.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import yuria.stackupper.Constants;
import yuria.stackupper.StackUpper;
import yuria.stackupper.StackUpperCommand;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ItemHandler {
    public static boolean determineTag(ItemStack itemStack, String tag)
    {
        return itemStack.is(new TagKey<>(BuiltInRegistries.ITEM.key(), ResourceLocation.parse(tag)));
    }

    public static void processSpecialData(ArrayList<UnfiliteredItemProperty> specialItemProperties)
    {
        BuiltInRegistries.ITEM.stream()
                .forEach(item -> {
                    if (item.equals(Items.AIR)) return;

                    ItemStack itemStack = new ItemStack(item);

                    specialItemProperties.forEach(i -> {
                        if (i instanceof UnfiliteredItemProperty.ItemPropertySize itemProperty) {
                            if (itemProperty.comparisonOperator.test(itemProperty.compareBy, itemStack.getMaxStackSize())) {
                                StackUpper.itemCollection.put(new ItemProperty(item, itemProperty.assignOperation, itemProperty.assingBy));
                            }
                        } else if (i instanceof UnfiliteredItemProperty.ItemPropertyId itemProperty) {

                            if (!Pattern.matches(itemProperty.toMatch, item.toString())) return;
                            StackUpper.itemCollection.put(new ItemProperty(item, itemProperty.assignOperation, itemProperty.assingBy));

                        } else if (i instanceof UnfiliteredItemProperty.TagItemProperty itemProperty) {
                            if (itemProperty.isRegex) {
                                itemStack.getTags().forEach(t -> {
                                    if (t.location().toString().matches(itemProperty.tag)) {
                                        StackUpper.itemCollection.put(new ItemProperty(item, itemProperty.assignOperation, itemProperty.assingBy));
                                    }
                                });
                                return;
                            }

                            itemStack.getTags().forEach(t -> {
                                if (t.equals(new TagKey<>(Registries.ITEM, ResourceLocation.parse(itemProperty.tag)))) {
                                    StackUpper.itemCollection.put(new ItemProperty(item, itemProperty.assignOperation, itemProperty.assingBy));
                                }
                            });
                        }

                    });
                });

    }

}
