/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package yuria.transformerlib;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.objectweb.asm.tree.ClassNode;
import portb.slw.MyLogger;
import portb.slw.MyLoggerFactory;
import yuria.transformerlib.xml.ClassTarget;
import yuria.transformerlib.xml.MethodTarget;
import yuria.transformerlib.xml.TransformerList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransformerLib
{
    public static        MyLogger          LOGGER       = MyLoggerFactory.emptyLogger();
    private static       List<ClassTarget> transformers = null;// new ArrayList<>();
    private final static XStream           xStream;
    
    static {
        xStream = new XStream(new StaxDriver());
        
        xStream.processAnnotations(new Class[] {TransformerList.class, MethodTarget.class, ClassTarget.class});
        xStream.allowTypesByWildcard(new String[]{"yuria.transformerlib.xml.*"});
        xStream.denyTypes(new Class[] {MethodTarget.MethodConverter.class});
        xStream.registerConverter(new MethodTarget.MethodConverter());
        xStream.registerConverter(new ClassTarget.DotReplacerConverter());
        
        RegisteredTransformerTypes.registerTransformer("slotLimit", BuiltinTransformers::fixSlotLimitMethodReturnValue);
        RegisteredTransformerTypes.registerTransformer("replace64",
                                                       BuiltinTransformers::replace64ConstantWithNewStackSize
        );
        RegisteredTransformerTypes.registerTransformer("replaceInt",
                                                       BuiltinTransformers::replaceIntegerConstantWithValue
        );
        RegisteredTransformerTypes.registerTransformer("replaceIntReturn", BuiltinTransformers::replaceIntReturnWithNewStack);
    }
    
    @SuppressWarnings("unused")
    public static void loadTransformers(Class<?> classContext)
    {
        LOGGER.debug("Loading transformers");

        transformers = new ArrayList<>();
        final String transformersDir = "/transformers";

        try
        {
            URI  uri = classContext.getResource(transformersDir).toURI();
            Path transformersPath;

            if (uri.getScheme().equals("jar"))
            {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap()))
                {
                    transformersPath = fileSystem.getPath(transformersDir);
                }
            }
            else
            {
                transformersPath = Paths.get(uri);
            }

            try(Stream<Path> fileWalk = Files.walk(transformersPath, 1))
            {
                for (Iterator<Path> it = fileWalk.iterator(); it.hasNext();)
                {
                    Path entry = it.next();

                    if(!entry.toString().equals(transformersDir))
                    {
                        try(
                                //typical java nonsense
                                InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(classContext.getResourceAsStream("/" + entry)));
                                BufferedReader br = new BufferedReader(isr);
                        )
                        {
                            String xml = br.lines().collect(Collectors.joining("\n"));

                            transformers.addAll(((TransformerList) xStream.fromXML(xml)).transformers);
                            LOGGER.info("Loaded transformer " + entry);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param classNode The classNode that might be transformed
     * @return True if the class was modified, false if not.
     */
    @SuppressWarnings("unused")
    public static boolean handleTransformation(ClassNode classNode)
    {
        boolean modified = false;
        
        for (ClassTarget target : transformers)
        {
            if(target.canAcceptClass(classNode))
            {
                LOGGER.debug("Transforming " + classNode.name + " (target of " + target.getTargetClassName() + ")");
                
                target.accept(classNode);
                
                modified = true;
                
                //loop does not terminate. a class can have multiple targets
            }
        }
        
        return modified;
    }
    
    @SuppressWarnings("unused")
    public static void setGlobalStackLimitSupplier(Supplier<Integer> supplier)
    {
        BuiltinTransformers.setGlobalStackSizeProducer(supplier);
    }
}
