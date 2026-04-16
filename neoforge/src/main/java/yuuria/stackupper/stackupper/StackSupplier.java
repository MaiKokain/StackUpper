package yuuria.stackupper.stackupper;


import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;

import java.util.Collections;
import java.util.function.Supplier;

public class StackSupplier {
    private static int MAX_STACK = 64;

    public static final Supplier<Integer> StackLimitSupplier = () -> MAX_STACK;

    public static int getMaxStack() { return MAX_STACK; }

    public static void setMaxStack(int new_max) { MAX_STACK = new_max; }

    public static void updateStackLimitFromData()
    {
        int max = StackUpperCommonConfig.SyncedServerConfig.CURRENT.global_max_stack();

        if (!StackUpper.SyncedServerItems.isEmpty())
        {
            max = Math.max(max, Collections.max(StackUpper.SyncedServerItems.values()));
        }

        setMaxStack(Math.max(max, 64));
    }
}
