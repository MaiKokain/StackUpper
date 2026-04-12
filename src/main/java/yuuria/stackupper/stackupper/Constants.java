package yuuria.stackupper.stackupper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.configlibrary.Property;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {
    public static final int BILLION   = 1_000_000_000;
    public static final int MILLION   = 1_000_000;
    public static final int THOUSAND  = 1_000;
    public static final DecimalFormat TOOLTIP_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    public static final File StackUpperConfigRuleset = new File(FMLPaths.CONFIGDIR.get().toFile().toString(), "stackupper");
    public static final Logger logger = LoggerFactory.getLogger("StackUpper");

    public static final Map<Item, Integer> SyncedServerSizes = new ConcurrentHashMap<>();

    public static HashMap<ResourceLocation, Integer>generateSyncHashMap()
    {
        StackSupplier.updateMaxStack();
        HashMap<ResourceLocation, Integer> sizesToSend = new HashMap<>();

        for (HashMap.Entry<Item, Property> entry : Constant.ItemCollection.entrySet()) {
            Item item = entry.getKey();
            Property prop = entry.getValue();

            int orig = prop.origStackSize.intValue();
            long returnedStackSize;

            if (prop.assignOperator != null) {
                if (prop.assignOperator.name().equals("EQUAL")) {
                    returnedStackSize = prop.assignOperator.apply(prop.assignedBy);
                } else {
                    returnedStackSize = prop.assignOperator.apply(prop.assignedBy, orig);
                }
                int finalSize = (int) Math.min(Math.max(returnedStackSize, 1), Integer.MAX_VALUE);

                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
                sizesToSend.put(itemId, finalSize);
            }
        }

        return sizesToSend;
    }
}
