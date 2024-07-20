package yuria.stackupper.config;

public class StackSize {
    private StackSize() {}

    public static int maxRegisteredItemStackSize = 64;
    public static final int MAX_ALLOWED = 1073741824;

    public static int getMaxStackSize()
    {
        return StackUpperConfig.itemStack;
    }

}
