package yuuria.stackupper.commands;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import yuuria.stackupper.StackUpper;

import java.util.ArrayList;

public class DebugCmds extends CommandTreeBase {
    private static ArrayList<String> debugCommands = new ArrayList<String>();
    private static String name = "stackupperdebug";
    public DebugCmds()
    {
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().acceptPackages("yuuria.stackupper.commands.debug").scan()) {
            for (ClassInfo classInfo : scanResult.getAllClasses()) {
                try {
                    Class<?> loadedClass = classInfo.loadClass();
                    if (loadedClass.isInterface()) continue;

                    if (CommandBase.class.isAssignableFrom(loadedClass)) {
                        CommandBase cmd = (CommandBase) loadedClass.getDeclaredConstructor().newInstance();
                        debugCommands.add(cmd.getName());
                        this.addSubcommand(cmd);
                    }
                } catch (ReflectiveOperationException e) {
                    StackUpper.LOGGER.error(e);
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/stackupperdebug ["+String.join("|", debugCommands)+"]";
    }
}
