package yuuria.stackupper.coremod;

import java.util.function.Supplier;

public class Utils {
    private static Supplier<Integer> globalMaxStackSizeProducer;

    public static int increaseStackSize(int original)
    {
        if (original == 99) return globalMaxStackSizeProducer.get() * (original / 99);
        return original;
    }

    public static void setGlobalMaxStackSizeProducer(Supplier<Integer> newGlobalMaxStackSizeProducer)
    {
        Utils.globalMaxStackSizeProducer = newGlobalMaxStackSizeProducer;
    }

    public static int getGlobalMaxStackSizeProducer()
    {
        return globalMaxStackSizeProducer.get();
    }
}
