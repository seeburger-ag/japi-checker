/*
 * Copyright 2011 William Bernardet
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
package com.googlecode.japi.checker.rules;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.JavaItem;

public class CheckInheritanceChanges implements Rule {

    private static final String JAVA_LANG_OBJECT_NAME = "java/lang/Object";

    @Override
    public void checkBackwardCompatibility(Reporter reporter,
            JavaItem reference, JavaItem newItem) {
        if (reference instanceof ClassData) {
            // Check extends...
            if (!doesExtend((ClassData)newItem, ((ClassData) reference).getSuperName())) {
                reporter.report(new Report(Severity.ERROR, reference.getName() + " extends " + ((ClassData) newItem).getSuperName() +
                        " and not " + ((ClassData) reference).getSuperName() + " anymore.", reference, newItem));
            }
            // Check interfaces
            for (String ifaceRef : ((ClassData) reference).getInterfaces()) {
                if (!hasInterface((ClassData)newItem, ifaceRef)) {
                    reporter.report(new Report(Severity.ERROR, reference.getName() + " is not implementing " + ifaceRef + " anymore.", reference, newItem));
                }
            }
        }
    }


    private boolean doesExtend(ClassData newItem, String superClassName)
    {
        if (newItem == null)
        {
            return false;
        }
        else if (newItem.getSuperName().equals(superClassName))
        {
            return true;
        }
        else if (!newItem.getSuperName().equals(JAVA_LANG_OBJECT_NAME))
        {
            return doesExtend(newItem.getClassDataLoader().fromName(newItem.getSuperName()), superClassName);
        }
        else
        {
            return false;
        }
    }

    private boolean hasInterface(ClassData newItem, String ifaceRef)
    {
        if (newItem == null)
        {
            return false;
        }
        else if (newItem.getInterfaces().contains(ifaceRef))
        {
            return true;
        }
        else if (!newItem.getSuperName().equals(JAVA_LANG_OBJECT_NAME))
        {
            return hasInterface(newItem.getClassDataLoader().fromName(newItem.getSuperName()), ifaceRef);
        }
        else
        {
            return false;
        }
    }

}
