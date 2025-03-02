package yuuria.stackupper.stackupper;

import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.util.tuples.Pair;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.configlibrary.Property;
import yuuria.stackupper.configlibrary.ast.AssignOperator;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Constants {
    public static final int BILLION   = 1_000_000_000;
    public static final int MILLION   = 1_000_000;
    public static final int THOUSAND  = 1_000;
    public static final DecimalFormat TOOLTIP_FORMAT = new DecimalFormat("###,###,###,###,###,###");

    public static final File StackUpperConfigRuleset = new File(FMLPaths.CONFIGDIR.get().toFile().toString(), "stackupper");

    public static final Logger logger = LoggerFactory.getLogger("StackUpper");

    public static final int MAX_STACK_LIMIT = 64;

    public static final Supplier<Integer> globalStackLimitSupplier = () -> {
        int max = MAX_STACK_LIMIT == StackUpperConfig.CONFIG.maxStackGlobally.getAsInt() ? MAX_STACK_LIMIT : StackUpperConfig.CONFIG.maxStackGlobally.getAsInt();
        if (Constant.ItemCollection.isEmpty()) return max;

        for (Property property : Constant.ItemCollection.values()) {
            if (property.assignOperator == AssignOperator.EQUAL) {
                max = Math.max(property.assignedBy.intValue(), max);
            } else {
                max = (int) Math.max(property.assignOperator.apply(property.assignedBy, property.origStackSize), max);
            }
        }

        return max;
    };
}
