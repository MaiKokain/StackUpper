/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package yuria.transformerlib.xml;

import com.thoughtworks.xstream.annotations.*;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuria.transformerlib.TransformerLib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
@XStreamAlias("class")
public class ClassTarget implements Consumer<ClassNode>
{
    @XStreamConverter(DotReplacerConverter.class)
    @XStreamAlias("path")
    @XStreamAsAttribute
    private String targetClassName;
    
    @XStreamAsAttribute
    private boolean transformChildren;
    
    @XStreamConverter(DotReplacerConverter.class)
    @XStreamImplicit
    @XStreamAlias("blacklist")
    private List<String> blacklistedClasses;
    
    @XStreamImplicit
    @XStreamAlias("method")
    private List<MethodTarget> methods;
    
    @XStreamOmitField
    private Set<String> knownSubtypes = null;
    
    @XStreamOmitField
    private boolean hasCheckedFields;
    
    private void checkFieldsAreInitialized()
    {
        if(hasCheckedFields)
            return;
        
        hasCheckedFields = true;
        
        if(knownSubtypes == null)
            knownSubtypes = new HashSet<>();
        
        if(blacklistedClasses == null)
            blacklistedClasses = new ArrayList<>();
        
        if(methods == null)
            //the hell is the transformer supposed to do if there are no targets??
            throw new RuntimeException(
                    "Transformer for " + targetClassName + "has no targets. What is it supposed to do? Use a genie?");
    }
    
    public boolean canAcceptClass(ClassNode classNode)
    {
        if(blacklistedClasses != null && blacklistedClasses.contains(classNode.name))
            return false;
        else if(transformChildren)
            return isClassInstanceOf(classNode);
        else
            return classNode.name.equals(targetClassName);
    }
    
    private boolean isClassInstanceOf(ClassNode classNode)
    {
        return classNode.name.equals(targetClassName)
                       || classNode.interfaces.contains(targetClassName)
                       || classNode.superName.equals(targetClassName)
                       || (knownSubtypes != null &&
                                   (
                                           knownSubtypes.contains(classNode.superName)
                                                   || classNode.interfaces.stream().anyMatch(knownSubtypes::contains)
                                   )
        );
    }
    
    @Override
    public void accept(ClassNode classNode)
    {
        if(transformChildren)
        {
            if(knownSubtypes == null)
                knownSubtypes = new HashSet<>();
            
            knownSubtypes.add(classNode.name);
        }
        
        for(MethodTarget target : methods)
        {
            if ("clinit".equals(target.getName())) target.setName("<clinit>");
            if ("init".equals(target.getName())) target.setName("<init>");

            Stream<MethodNode> stream = classNode.methods.stream()
                                                         .filter(methodNode -> methodNode.name.equals(target.getName()));
            
            if (target.getDescriptor() != null)
                stream = stream.filter(it -> it.desc.equals(target.getDescriptor()));
            
            List<MethodNode> nodes = stream.collect(Collectors.toList());
            
            if (nodes.size() > 1)
            {
                //fail hard - if there are multiple methods matched, you should use descriptors to specify which one should be changed.
                throw new RuntimeException(
                        "Multiple methods matched for " + target.getName() + " [desc: " + target.getDescriptor() +
                                "] in " + classNode.name);
            }
            else if (nodes.size() == 1)
            {
                target.transform(classNode, nodes.get(0));
            }
        }
    }
    
    public String getTargetClassName()
    {
        return targetClassName;
    }
    
    /**
     * Replaces '.'s with '/'s for target path. Using '.'s allows IDE to autocomplete.
     */
    public static class DotReplacerConverter implements SingleValueConverter
    {
        public DotReplacerConverter()
        {
        }
        
        @Override
        public String toString(Object obj)
        {
            return obj.toString();
        }
        
        @Override
        public Object fromString(String str)
        {
            return str.replace('.', '/');
        }
        
        @Override
        public boolean canConvert(Class type)
        {
            return type == String.class;
        }
    }
    
    @Override
    public String toString()
    {
        return "ClassTarget{" +
                       "targetClassName='" + targetClassName + '\'' +
                       ", transformChildren=" + transformChildren +
                       ", blacklistedClasses=" + blacklistedClasses +
                       ", methods=" + methods +
                       ", knownSubtypes=" + knownSubtypes +
                       ", hasCheckedFields=" + hasCheckedFields +
                       '}';
    }
}
