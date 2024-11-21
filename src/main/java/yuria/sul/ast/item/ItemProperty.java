package yuria.sul.ast.item;

import net.minecraft.world.item.Item;
import yuria.sul.ast.AssignOperation;

public class ItemProperty {
    public final Item item;
    public final AssignOperation assignOperation;
    public final Integer doOpBy;

    public ItemProperty(Item item, AssignOperation operation, Integer doOpBy) {
        this.assignOperation = operation;
        this.doOpBy = doOpBy;
        this.item = item;
    }
}
