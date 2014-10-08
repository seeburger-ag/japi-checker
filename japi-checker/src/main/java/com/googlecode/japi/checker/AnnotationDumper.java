/*
 * Copyright 2013 William Bernardet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.japi.checker;

import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import com.googlecode.japi.checker.model.AnnotationData;
import com.googlecode.japi.checker.model.JavaItem;

class AnnotationDumper extends AnnotationVisitor {
    private Logger logger = Logger.getLogger(AnnotationDumper.class.getName());
    private AnnotationData annotation;

    public AnnotationDumper(JavaItem owner, String desc, boolean visible) {
        super(Opcodes.ASM4);
        logger.fine("    (annotation) " + desc + " " + visible);
        this.annotation = new AnnotationData(desc, visible); 
        owner.add(annotation);
    }

    @Override
    public void visit(String name, Object value) {
        this.annotation.put(name, value);
    }
    
}