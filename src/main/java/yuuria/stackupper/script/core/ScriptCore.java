package yuuria.stackupper.script.core;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ScriptCore {
    @SerializedName(value = "stack", alternate = {"amount", "value", "size"})
    private int stack;
    @JsonAdapter(AssignAdapter.class)
    private String assign;
    private List<FilterBase> filter;

    public int getStack() { return stack; }
    public String getAssign() { return assign; }
    public List<FilterBase> getFilter() { return filter; }

    public AssignOp getAssignOp()
    {
        return AssignOp.valueOf(assign.toUpperCase(Locale.ROOT));
    }

    public enum AssignOp {
        SET, ADD, SUB, MULTI, DIV;

        public int apply(int current, int operand)
        {
            return switch (this) {
                case SET -> operand;
                case ADD -> current + operand;
                case DIV -> operand == 0 ? current : current / operand;
                case SUB -> current - operand;
                case MULTI -> current * operand;
                default -> current;
            };
        }
    }

    private static class AssignAdapter extends TypeAdapter<String> {
        private static final List<String> ALLOWED = Arrays.asList("MULTI", "ADD", "SUB", "DIV", "SET");

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            String value = in.nextString();
            if (!ALLOWED.contains(value)) throw new JsonParseException("Invalid assign value: " + value);
            return value;
        }
    }
}
