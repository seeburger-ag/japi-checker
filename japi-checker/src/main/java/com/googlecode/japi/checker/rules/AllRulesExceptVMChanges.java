/*
 * AllRulesExceptVMChanges.java
 *
 * created at 11.05.2015 by Rico Neubauer <r.neubauer@seeburger.de>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.googlecode.japi.checker.rules;

import java.util.Iterator;

import com.googlecode.japi.checker.Rule;


public class AllRulesExceptVMChanges extends AllRules
{

    public AllRulesExceptVMChanges()
    {
        // no simple rules.remove(...) possible
        for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext();)
        {
            Rule rule = iterator.next();
            if (rule instanceof CheckClassVersion)
            {
                iterator.remove();
            }
        }
    }

}
