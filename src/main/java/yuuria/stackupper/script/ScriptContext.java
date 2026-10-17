package yuuria.stackupper.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistry;
import yuuria.stackupper.StackUpperHelpers;
import yuuria.stackupper.script.core.FilterBase;
import yuuria.stackupper.script.core.FilterDeserializer;
import yuuria.stackupper.script.core.ScriptCore;

import java.io.FileReader;

public class ScriptContext {
    private final IForgeRegistry<Item> registry;
    private final FileReader fileReader;

    public ScriptContext(IForgeRegistry<Item> registry, FileReader fileReader) {
        this.registry = registry;
        this.fileReader = fileReader;
    }

    public void execute()
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(FilterBase.class, new FilterDeserializer())
                .setPrettyPrinting()
                .create();
        ScriptCore scriptCore = gson.fromJson(fileReader, ScriptCore.class);
        if (scriptCore == null || scriptCore.getFilter() == null) return;

        ScriptCore.AssignOp assignOp = scriptCore.getAssignOp();
        int operand = scriptCore.getStack();

        for (Item item : registry) {
            ItemStack itemStack = new ItemStack(item);
            boolean matched = false;
            for (FilterBase filterBase : scriptCore.getFilter()) {
                if (filterBase.test(item, itemStack)) {
                    matched = true;
                    break;
                }
            }

            if (!matched) continue;

            int current = item.getItemStackLimit(itemStack);
            int newLimit = assignOp.apply(current, operand);
            newLimit = MathHelper.clamp(newLimit, 1, Integer.MAX_VALUE);

            if (!StackUpperHelpers.appliedStackMap.containsKey(item)) {
                StackUpperHelpers.appliedStackMap.put(item, newLimit);
            }


        }
    }
}
