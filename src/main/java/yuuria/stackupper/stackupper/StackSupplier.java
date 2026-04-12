package yuuria.stackupper.stackupper;

import net.neoforged.neoforge.network.PacketDistributor;
import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.configlibrary.Property;
import yuuria.stackupper.configlibrary.ast.AssignOperator;
import yuuria.stackupper.stackupper.network.SyncStackSizesPayload;

import java.util.Collections;
import java.util.function.Supplier;

public class StackSupplier {
    private static int MAX_STACK = 64;

    public static final Supplier<Integer> globalStackLimitSupplier = () -> {
        if (!StackUpperConfig.CONFIG_SPEC.isLoaded()) {
            return MAX_STACK;
        }
        return MAX_STACK;
    };

    public static int getMaxStack() {
        return MAX_STACK;
    }

    public static void setMaxStack(int max) {
        MAX_STACK = max;
    }

    public static void updateMaxStack() {
        int max = 64 == StackUpperConfig.CONFIG.maxStackGlobally.getAsInt()
                ? 64
                : StackUpperConfig.CONFIG.maxStackGlobally.getAsInt();

        if (!Constants.SyncedServerSizes.isEmpty()) {
            max = Math.max(max, Collections.max(Constants.SyncedServerSizes.values()));
        }
        if (!Constant.ItemCollection.isEmpty()) {
            for (Property property : Constant.ItemCollection.values()) {
                if (property.assignOperator == AssignOperator.EQUAL) {
                    max = Math.max(property.assignedBy.intValue(), max);
                } else {
                    max = (int) Math.max(property.assignOperator.apply(property.assignedBy, property.origStackSize), max);
                }
            }
        }

        setMaxStack(Math.max(max, 64));
    }
}
