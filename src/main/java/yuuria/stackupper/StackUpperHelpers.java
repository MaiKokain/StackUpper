package yuuria.stackupper;

import com.google.gson.Gson;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.item.Item;

import java.io.File;
import java.util.Random;

public final class StackUpperHelpers {
    public static File STACKUP_FOLDER;
    public static final Random RANDOM = new Random();
    public static TObjectIntMap<Item> appliedStackMap = new TObjectIntHashMap<>();
    public static int[] trueConf = new int[]{1, 1, 16, 64}; // advancedFilter, ignoreAdvFilter, stack =16, stack =64

    private StackUpperHelpers() {}
    @SuppressWarnings("unused")
    public static int getMaxStackSize() {
        return Integer.MAX_VALUE;
    }

    public static String globToRegex(String glob) {
        StringBuilder sb = new StringBuilder("^");
        for (char c : glob.toCharArray()) {
            switch (c) {
                case '*': sb.append(".*"); break;
                case '?': sb.append('.');  break;
                case '.': case '(': case ')': case '+': case '|':
                case '^': case '$': case '\\': case '[': case ']':
                case '{': case '}':
                    sb.append('\\').append(c); break;
                default:
                    sb.append(c);
            }
        }
        return sb.append('$').toString();
    }

    public static int[] generateConfigInt()
    {
        StackUpper.LOGGER.info("hmm {}", new Gson().toJson(StackUpper.CONFIG));
        int[] cfg = new int[4];
        cfg[0] = StackUpper.CONFIG.isAdvancedFilter() ? 1 : 0;
        cfg[1] = StackUpper.CONFIG.isIgnoreAdvFilter() ? 1 : 0;
        cfg[2] = StackUpper.CONFIG.getStack16();
        cfg[3] = StackUpper.CONFIG.getStack64();
        StackUpper.LOGGER.info("generateConfigInt {}", cfg);
        return cfg;
    }

}