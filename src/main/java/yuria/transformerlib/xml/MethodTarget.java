/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package yuria.transformerlib.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yuria.transformerlib.RegisteredTransformerTypes;
import yuria.transformerlib.TransformationExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@XStreamAlias("method")
@XStreamConverter(MethodTarget.MethodConverter.class)
public class MethodTarget
{
    //XML attr name: path
    private String name;
    
    //XML attr name: desc
    //may be null
    private String descriptor;
    
    //XML attr name: method
    private TransformationExecutor transformationExecutor;
    
    //Not an explicit attribute, instead is the collection of all attributes for the target
    private Map<String, String> parameters;
    
    public String getName()
    {
        return name;
    }

    public String setName(String name)
    {
        this.name = name;
        return this.name;
    }

    public String getDescriptor()
    {
        return descriptor;
    }
    
    public void transform(ClassNode classNode, MethodNode methodNode)
    {
        transformationExecutor.transform(classNode, methodNode, parameters);
    }
    
    public static class MethodConverter implements Converter
    {
        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
        {
        
        }
        
        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
        {
            MethodTarget method = new MethodTarget();
            
            method.name = attributeOrThrow(reader, "name", "Name attribute missing");
            String transformerName = attributeOrThrow(reader, "transformer", "Transformer attribute missing");
            
            method.descriptor = reader.getAttribute("desc");
            
            if (!RegisteredTransformerTypes.isTransformerValid(transformerName))
                throw new ConversionException("Transformer " + transformerName + " is not valid");
            
            method.transformationExecutor = RegisteredTransformerTypes.getTransformer(transformerName);
            
            //read all the attributes on the node and put them into the parameters variable for potential use with specifying behaviour for the transformer
            Map<String, String>                             parameters = new HashMap<>();
            @SuppressWarnings("unchecked") Iterator<String> itr        = reader.getAttributeNames();
            
            while(itr.hasNext())
            {
                String name = itr.next();
                parameters.put(name, reader.getAttribute(name));
            }
            
            method.parameters = parameters;
            
            return method;
        }
        
        private static String attributeOrThrow(HierarchicalStreamReader reader, String attrName, String message)
        {
            String attr = reader.getAttribute(attrName);
            
            if(attr == null)
                throw new ConversionException(message);
            
            return attr;
        }
        
        @Override
        public boolean canConvert(Class type)
        {
            return type.equals(MethodTarget.class);
        }
    }
    
    
}
