package yuuria.stackupper.stackupper;

import yuuria.stackupper.configlibrary.Constant;
import yuuria.stackupper.configlibrary.Property;
import yuuria.stackupper.configlibrary.ast.AssignOperator;

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
        if (Constant.ItemCollection.isEmpty()) {
            setMaxStack(max);
            return;
        }

        for (Property property : Constant.ItemCollection.values()) {
            if (property.assignOperator == AssignOperator.EQUAL) {
                max = Math.max(property.assignedBy.intValue(), max);
            } else {
                max = (int) Math.max(property.assignOperator.apply(property.assignedBy, property.origStackSize), max);
            }
        }

        setMaxStack(max);
    }
}
