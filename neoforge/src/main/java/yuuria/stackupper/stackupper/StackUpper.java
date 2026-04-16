package yuuria.stackupper.stackupper;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import yuuria.stackupper.configlibrary.StackUpperConfigLibraryFunctionWrapper;
import yuuria.stackupper.configlibrary.core.enums.AssignOperator;
import yuuria.stackupper.configlibrary.core.interfaces.StackRule;
import yuuria.stackupper.stackupper.config.StackUpperClientConfig;
import yuuria.stackupper.stackupper.config.StackUpperCommonConfig;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Mod(StackUpper.MODID)
public class StackUpper {
    static {
        StackUpperConfigLibraryFunctionWrapper.setProcessRules(StackUpper::processRules);
    }

    public static final String MODID = "stackupper";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final File StackUpperConfigRuleset = new File(FMLPaths.CONFIGDIR.get().toFile().toString(), "stackupper");
    public static final Map<Item, Integer> SyncedServerItems = new ConcurrentHashMap<Item, Integer>();

    public StackUpper(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.CLIENT, StackUpperClientConfig.CONFIG_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, StackUpperCommonConfig.CONFIG_SPEC);
    }

    public static HashMap<Identifier, Integer>generateDataToSync()
    {
        StackSupplier.updateStackLimitFromData();

        HashMap<Identifier, Integer> dataToSend = new HashMap<>();

        SyncedServerItems.forEach((i, size) -> {
            dataToSend.put(BuiltInRegistries.ITEM.getKey(i), size);
        });

        return dataToSend;
    }

    public static void processRules(List<StackRule> rules) {
        SyncedServerItems.clear();
        for (StackRule rule : rules) {
            StackUpper.LOGGER.info("type={}", rule.toString());
            if (rule instanceof StackRule.IdRule idRule) {
                Stream<Item> item = BuiltInRegistries.ITEM.stream()
                        .filter(i -> !i.equals(Items.AIR))
                        .filter(i -> { return Pattern.matches(idRule.resource_id(), i.toString()); });
                item.forEach(s -> {
                    if (idRule.assign_op() == AssignOperator.EQUAL) {
                        SyncedServerItems.put(s, idRule.assign_op().apply(idRule.assign_op_input()).intValue());
                    } else {
                        SyncedServerItems.put(s, idRule.assign_op().apply(s.getDefaultMaxStackSize(), idRule.assign_op_input()).intValue());
                    }
                });
                item.close();

            }

            if (rule instanceof StackRule.TagRule tagRule) {
                Stream<Item> item = BuiltInRegistries.ITEM.stream()
                        .filter(i -> !i.equals(Items.AIR))
                        .filter(i -> {
                            ItemStack item_stack = new ItemStack(i);
                            var s = item_stack.tags().toList().stream().filter(i_tagkey -> i_tagkey.location().toString().matches(tagRule.tag())).toList();
                            return !s.isEmpty();
                        });
                item.forEach(s -> {
                    if (tagRule.assign_op() == AssignOperator.EQUAL) {
                        SyncedServerItems.put(s, tagRule.assign_op().apply(tagRule.assign_op_input()).intValue());
                    } else {
                        SyncedServerItems.put(s, tagRule.assign_op().apply(s.getDefaultMaxStackSize(), tagRule.assign_op_input()).intValue());
                    }
                });
                item.close();
            }

            if (rule instanceof StackRule.SizeRule sizeRule) {
                Stream<Item> item = BuiltInRegistries.ITEM.stream()
                        .filter(i -> !i.equals(Items.AIR))
                        .filter(i -> sizeRule.compare_op().test(sizeRule.compare_op_input(), i.getDefaultMaxStackSize()));

                item.forEach(s -> {
                    if (sizeRule.assign_op() == AssignOperator.EQUAL) {
                        SyncedServerItems.put(s, sizeRule.assign_op().apply(sizeRule.assign_op_input()).intValue());
                    } else {
                        SyncedServerItems.put(s, sizeRule.assign_op().apply(s.getDefaultMaxStackSize(), sizeRule.assign_op_input()).intValue());
                    }
                });
                item.close();

            }
        }
    }
}