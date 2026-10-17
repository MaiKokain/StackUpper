package yuuria.stackupper.script;

import com.google.common.collect.Ordering;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import yuuria.stackupper.StackUpper;

import java.io.File;
import java.io.FileReader;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class ScriptHandler {

    protected void processFile(IForgeRegistry<Item> registry, File file)
    {
        if (!file.getName().endsWith(".json")) return;
        StackUpper.LOGGER.info("Processing {}", file.getName());

        try (FileReader fileReader = new FileReader(file)) {
            new ScriptContext(registry, fileReader).execute();
        } catch (Exception e) {
            StackUpper.LOGGER.error(e);
        }
//        applyChanges();
    }

    protected void processDir(IForgeRegistry<Item> registry, File file)
    {
        Set<File> fileSet = new TreeSet<>(Ordering.natural());
        for (File file1 : Objects.requireNonNull(file.listFiles())) {
            fileSet.add(file1);
        }

        for (File file1 : fileSet) {
            if (file1.isDirectory()) {
                processDir(registry, file1);
            } else {
                processFile(registry, file1);
            }
        }
    }

    public void process(IForgeRegistry<Item> registry, File baseDir)
    {
        if (baseDir == null || !baseDir.exists() || !baseDir.isDirectory() || !StackUpper.CONFIG.isAdvancedFilter()) return;
        processDir(registry, baseDir);
    }

//    protected void applyChanges()
//    {
//        for (Item item : StackUpHelpers.appliedStackMap.keySet()) {
//            if (item == Items.AIR) continue;
//
//            int target = StackUpHelpers.appliedStackMap.get(item);
//            StackUpHelpers.addToOriginalMap(item);
//            StackUp.LOGGER.info("Tried applying {} to {}", item.getRegistryName(), target);
//            item.setMaxStackSize(target);
//            int res = item.getItemStackLimit();
//            if (target != res) {
//                StackUp.LOGGER.warn("{} stack size weren't changed", item.getRegistryName());
//            }
//        }
//
//        StackUpHelpers.appliedStackMap.clear();
//    }
}
