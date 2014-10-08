/*
 * CheckMinorVersionIncreaseNeeded.java
 *
 * created at Oct 7, 2014 by akoledzhikov <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.googlecode.japi.checker.rules;


import java.util.HashSet;
import java.util.Set;

import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Rule;
import com.googlecode.japi.checker.Scope;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.model.ClassData;
import com.googlecode.japi.checker.model.FieldData;
import com.googlecode.japi.checker.model.JavaItem;
import com.googlecode.japi.checker.model.MethodData;


public class CheckMinorVersionIncreaseNeeded
    implements Rule
{
    private boolean minorVersionIncreased;

    private boolean versionIncreaseNecessary;


    @Override
    public void checkBackwardCompatibility(Reporter reporter, JavaItem reference, JavaItem newItem)
    {
        if (reference instanceof ClassData)
        {
            ClassData oldCD = (ClassData)reference;
            ClassData newCD = (ClassData)newItem;
            Set<MethodData> oldMethods = new HashSet<MethodData>(oldCD.getMethods());
            Set<FieldData> oldFields = new HashSet<FieldData>(oldCD.getFields());

            for (MethodData md : newCD.getMethods())
            {
                if (md.getVisibility().equals(Scope.PUBLIC))
                {
                    if (!oldMethods.contains(md))
                    {
                        versionIncreaseNecessary = true;
                        if (!minorVersionIncreased)
                        {
                            reporter.report(new Report(Severity.ERROR, "Adding the new public method " + md.toString() + " warrants a minor version increase!", reference, newItem));
                        }
                    }
                }
            }

            for (FieldData fd : newCD.getFields())
            {
                if (fd.getVisibility().equals(Scope.PUBLIC))
                {
                    if (!oldFields.contains(fd))
                    {
                        versionIncreaseNecessary = true;
                        if (!minorVersionIncreased)
                        {
                            reporter.report(new Report(Severity.ERROR, "Adding the new public field " + fd.toString() + " warrants a minor version increase!", reference, newItem));
                        }
                    }
                }
            }
        }
    }


    public void setMinorVersionIncreased(boolean minorVersionIncreased)
    {
        this.minorVersionIncreased = minorVersionIncreased;
    }


    public boolean isVersionIncreaseNecessary()
    {
        return versionIncreaseNecessary;
    }
}
