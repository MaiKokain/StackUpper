package yuria.stackupper;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import yuria.sul.ast.core.Processor;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = StackUpper.MODID)
public class StackUpperCommand {
    @SubscribeEvent
    public static void onRegisterCommand(final RegisterCommandsEvent event)
    {
        var command = Commands.literal("stackupper");

        String debugProp = System.getProperty("stackupper.debug");

        if (debugProp != null) {
            command.then(Commands.literal("print_item_cached")
                    .requires(s -> s.hasPermission(0))
                    .executes(ctx -> {
                        if (StackUpper.itemCollection.cache.isEmpty()) {
                            StackUpper.LOGGER.error("ItemCollection is empty");
                            return 0;
                        }
                        StackUpper.itemCollection.cache.forEach((k,v) -> StackUpper.LOGGER.info("item {} operator {} do-by {}", k.toString(), v.assignOperation, v.doOpBy));
                        return SINGLE_SUCCESS;
                    })
            );

            command.then(Commands.literal("print_mc_item_tags")
                    .requires(s -> s.hasPermission(0))
                    .executes(ctx -> {
                        BuiltInRegistries.ITEM.forEach(i -> {
                            ItemStack itemStack = new ItemStack(i);
                            itemStack.getTags().forEach(i1 -> StackUpper.LOGGER.info("{} {}", i, i1));
                        });
                        return 0;
                    })
            );

            command.then(Commands.literal("print_to_be_processed")
                    .requires(s -> s.hasPermission(0))
                    .executes(ctx -> {
                        for (Object object : Processor.listener.toBeProcessed) {
                            StackUpper.LOGGER.info("object class {}", object.getClass());
                        }
                        return 0;
                    })
            );
        }

        command.then(Commands.literal("reload")
                .requires(s -> s.hasPermission(1))
                .executes(ctx -> {
                    if (!StackUpperConfig.CONFIG.enableScripting.get() && Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.displayClientMessage(Component.literal("Scripting is disabled."), false);
                        return 1;
                    }
                    if (!StackUpper.itemCollection.cache.isEmpty()) StackUpper.itemCollection.clear();
                    StackUpper.astProccessor.processFileToArray(StackUpper.StackUpperLangFolder);
                    StackUpper.astProccessor.start();
                    return 0;
                })
        );

        event.getDispatcher().register(command);
    }
}
