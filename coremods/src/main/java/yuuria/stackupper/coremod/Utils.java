package yuuria.stackupper.coremod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Utils {
    private static Logger logger = LoggerFactory.getLogger("StackUpperCoreMod");
    private static Supplier<Integer> globalMaxStackSizeProducer;

    public static int increaseStackSize(int original)
    {
//        if (original == 99 || original == 64) return getGlobalMaxStackSizeProducer();
        return getGlobalMaxStackSizeProducer();
    }

    public static int return32I() {
        return Integer.MAX_VALUE;
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
