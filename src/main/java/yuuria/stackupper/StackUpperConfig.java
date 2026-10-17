package yuuria.stackupper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class StackUpperConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static Path FILE;

    public static StackUpperConfig init(Path confDir)
    {
        FILE = confDir.resolve("stackupper.json");
        return read();
    }

    public static StackUpperConfig read()
    {
        if (!Files.notExists(FILE)) {
            try (Reader reader = Files.newBufferedReader(FILE)) {
                StackUpperConfig config = GSON.fromJson(reader, StackUpperConfig.class);
                if (config == null) {
                    StackUpper.LOGGER.error("Config is empty");
                } else {
                    return config;
                }
            } catch (Exception e) {
                StackUpper.LOGGER.error("Unable to read config", e);
            }
        } else {
            new StackUpperConfig().save();
        }
        return new StackUpperConfig();
    }

    @SerializedName("stackOf16")
    private int stack16 = 16;
    @SerializedName("stackOf64")
    private int stack64 = 64;
    @SerializedName("advancedScripting")
    private boolean advancedFilter = false;
    @SerializedName("returnStackEvenWithAdvScripting")
    private boolean ignoreAdvFilter = false;
    @SerializedName("scaleTextLinearly")
    private boolean scaleTextLinearly = false;
    @SerializedName("highestScaleDown")
    private float highestScaleDown = 1.0f;
    @SerializedName("lowestScaleDown")
    private float lowestScaleDown = 0.0f;

    public int getStack16() {return stack16;}
    public int getStack64() {return stack64;}

    public boolean isAdvancedFilter() {return advancedFilter;}
    public boolean isIgnoreAdvFilter() {return ignoreAdvFilter;}

    public boolean isScaleTextLinearly() {return scaleTextLinearly;}
    public float getHighestScaleDown() {return highestScaleDown;}
    public float getLowestScaleDown() {return lowestScaleDown;}

    public void setAdvancedFilter(boolean advancedFilter) {this.advancedFilter = advancedFilter;}
    public void setIgnoreAdvFilter(boolean ignoreAdvFilter) {this.ignoreAdvFilter = ignoreAdvFilter;}
    public void setStack16(int stack16) {this.stack16 = stack16;}
    public void setStack64(int stack64) {this.stack64 = stack64;}

    public void save()
    {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            StackUpper.LOGGER.error("Unable to save config", e);
        }
    }
}