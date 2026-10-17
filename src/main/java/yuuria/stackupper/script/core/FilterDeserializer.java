package yuuria.stackupper.script.core;

import com.google.gson.*;
import yuuria.stackupper.script.core.filters.ModFilter;
import yuuria.stackupper.script.core.filters.OreDictFilter;
import yuuria.stackupper.script.core.filters.ResourceLocationFilter;
import yuuria.stackupper.script.core.filters.SizeFilter;

import java.lang.reflect.Type;
import java.util.HashMap;

public class FilterDeserializer implements JsonDeserializer<FilterBase> {
    private static HashMap<String, Class<? extends FilterBase>> FILTERS = new HashMap<>();
    static {
        FILTERS.put("mod", ModFilter.class);
        FILTERS.put("oredict", OreDictFilter.class);
        FILTERS.put("id", ResourceLocationFilter.class);
        FILTERS.put("size", SizeFilter.class);
    }

    @Override
    public FilterBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();

        Class clazz = FILTERS.get(type);
        if (clazz == null) throw new JsonParseException("Unknown filter type: " + type);
        return context.deserialize(obj, clazz);
    }
}
