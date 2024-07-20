/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package yuria.transformerlib;


import java.util.HashMap;

public class RegisteredTransformerTypes
{
    private static final HashMap<String, TransformationExecutor> registeredTransformers = new HashMap<>();
    
    public static void registerTransformer(String name, TransformationExecutor method)
    {
        registeredTransformers.put(name, method);
    }
    
    public static boolean isTransformerValid(String name)
    {
        return registeredTransformers.containsKey(name);
    }
    
    public static TransformationExecutor getTransformer(String name)
    {
        return registeredTransformers.get(name);
    }
}
