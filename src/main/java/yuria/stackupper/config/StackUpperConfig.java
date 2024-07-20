package yuria.stackupper.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class StackUpperConfig extends MidnightConfig {
    @Entry(category = "numbers", min = 64, max = StackSize.MAX_ALLOWED, name = "Max item size")
    public static int itemStack = 64;
}
