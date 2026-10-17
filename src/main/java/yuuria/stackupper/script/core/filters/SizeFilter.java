package yuuria.stackupper.script.core.filters;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import yuuria.stackupper.StackUpper;
import yuuria.stackupper.script.core.FilterBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SizeFilter extends FilterBase {
    @JsonAdapter(CompareAdapter.class)
    private String compare;
    public String getCompare() { return compare; }

    @Override
    public boolean test(Item item, ItemStack stack) {
        int count = stack != null ? stack.getMaxStackSize() : 1;
        int parsed;
        try {
            parsed = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            StackUpper.LOGGER.error(e);
            return false;
        }
//        StackUp.LOGGER.info("sized tested with {} {} {}", count, compare, parsed);
        return CompareOp.valueOf(compare).apply(count, parsed);
    }

    private static class CompareAdapter extends TypeAdapter<String>
    {
        private static final List<String> ALLOWED = Arrays.asList("LESS_THAN", "LESS_THAN_EQUAL", "MORE_THAN", "MORE_THAN_EQUAL", "NOT_EQUAL", "EQUAL");
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            String value = in.nextString();
            if (!ALLOWED.contains(value)) throw new JsonParseException("Invalid compare value: " + value);
            return value;
        }
    }

    private enum CompareOp {
        LESS_THAN,
        LESS_THAN_EQUAL,
        MORE_THAN,
        MORE_THAN_EQUAL,
        NOT_EQUAL,
        EQUAL;

        public boolean apply(int count, int value)
        {
            return switch (this) {
                case EQUAL -> count == value;
                case LESS_THAN -> count < value;
                case LESS_THAN_EQUAL -> count <= value;
                case MORE_THAN -> count > value;
                case MORE_THAN_EQUAL -> count >= value;
                case NOT_EQUAL -> count != value;
                default -> false;
            };
        }
    }
}
