package yuuria.stackupper.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.*;
import yuuria.stackupper.StackUpperCore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class StackUpperTransformer implements IClassTransformer {
    public static final Logger LOGGER = LogManager.getLogger("StackUpperCore");
    private final List<IStackUpPatch> callableClass = new ArrayList<>();

    public StackUpperTransformer() {

        try (ScanResult scanResult = new ClassGraph().enableClassInfo().acceptPackages("yuuria.stackupper.core.patches").scan()) {
            for (ClassInfo classInfo : scanResult.getAllClasses()) {
                try {
                    Class<?> loadedClass = classInfo.loadClass();
                    if (loadedClass.isInterface()) continue;

                    if (IStackUpPatch.class.isAssignableFrom(loadedClass)) {
                        IStackUpPatch IStackUpPatch = (IStackUpPatch) loadedClass.getDeclaredConstructor().newInstance();
                        LOGGER.info("added {}", loadedClass.getName());
                        callableClass.add(IStackUpPatch);
                    }
                } catch (ReflectiveOperationException e) {
                    LOGGER.error(e);
                }
            }
        }
    }

    @Override
    public byte[] transform(String s, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        ClassNode classNode = null;
        for (IStackUpPatch patches : callableClass) {
            if (classNode == null) {
                classNode = new ClassNode();
                new ClassReader(bytes).accept(classNode, 0);
            }
            if (!patches.canExecute(classNode, transformedName) || !patches.canExecute(classNode)) continue;

            try {
                patches.execute(classNode);
            } catch (Throwable e) {
                LOGGER.error("Patch {} failed on {}", patches.getClass().getName(), transformedName, e);
            }
        }
        if (classNode == null) return bytes;

        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
