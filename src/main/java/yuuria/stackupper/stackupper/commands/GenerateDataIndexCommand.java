package yuuria.stackupper.stackupper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;
import org.checkerframework.checker.nullness.qual.Nullable;
import yuuria.stackupper.stackupper.Constants;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GenerateDataIndexCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> ctx)
    {
        var builder = Commands.literal("generate_data_index").requires(c -> c.hasPermission(4));

        builder.executes(c -> generate_data_index(c, null));

        builder.then(
            Commands.argument("filter_mod", StringArgumentType.word())
                    .suggests(GenerateDataIndexCommand::suggestModIDs)
                    .executes(c -> generate_data_index(c, StringArgumentType.getString(c, "filter_mod")))
        );

        ctx.then(builder);
    }

    private static int generate_data_index(CommandContext<CommandSourceStack> ctx, @Nullable String filter_mod)
    {
        Path INDEX_COMMAND_PATH = generateFilePath();
        HashMap<Item, List<String>> mappings = new HashMap<Item, List<String>>();
        var items = get_items(filter_mod);
        if (items.isEmpty()) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Generate data index failed, Items is empty").withStyle(ChatFormatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }
        for (Item item : items) {
            mappings.put(item, item.getDefaultInstance().getTags().map(itemTagKey -> itemTagKey.location().toString()).toList());
        }

        try {
            Files.deleteIfExists(INDEX_COMMAND_PATH);
        } catch (IOException e) {
            Constants.logger.error("Failed to delete indexed path");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(INDEX_COMMAND_PATH, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            mappings.forEach((item, tags) -> {
                try {
                    writer.write(item.toString() + ";");
                    if (!tags.isEmpty()) {
                        writer.write(String.join(",", tags));
                    }
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MutableComponent textComponent = Component.literal(INDEX_COMMAND_PATH.toAbsolutePath().toString());
        textComponent.withStyle(ChatFormatting.UNDERLINE);

        MutableComponent msg = Component.literal("Data Index generation done! Located at ");
        msg.append(textComponent);

        if (ctx.getSource().getPlayer() != null) {
            ctx.getSource().getPlayer().sendSystemMessage(msg);
        } else {
            Constants.logger.info(String.format("Data index generation done! Located at %s", INDEX_COMMAND_PATH.toAbsolutePath()));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static List<Item> get_items(@Nullable String filter)
    {
        if (filter != null) {
            return BuiltInRegistries.ITEM.stream()
                    .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(filter))
                    .toList();
        } else {
            return BuiltInRegistries.ITEM.stream().toList();
        }
    }

    private static CompletableFuture<Suggestions> suggestModIDs(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder)
    {
        var modIds = ModList.get().getMods().stream().map(IModInfo::getModId).toList();
        return SharedSuggestionProvider.suggest(modIds, builder);
    }

    private static Path generateFilePath()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = dtf.format(LocalDateTime.now());
        return Path.of(FMLPaths.GAMEDIR.get().toString(), "logs", "stackupper_"+timestamp+".sud");
    }

}
