/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package yuria.transformerlib;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Map;

public interface TransformationExecutor
{
    void transform(ClassNode classNode, MethodNode methodNode, Map<String, String> parameters);
}
