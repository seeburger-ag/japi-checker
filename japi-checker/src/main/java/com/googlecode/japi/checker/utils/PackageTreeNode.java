/*
 * PackageTreeNode.java
 *
 * created at Oct 9, 2014 by akoledzhikov <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package com.googlecode.japi.checker.utils;


import java.util.HashMap;
import java.util.Map;


public class PackageTreeNode
{
    private boolean exclude;

    private String name;

    private boolean excludeChildren;

    private Map<String, PackageTreeNode> children;


    public PackageTreeNode(String name, boolean exclude)
    {
        super();
        this.exclude = exclude;
        this.name = name;
        children = new HashMap<String, PackageTreeNode>();
    }


    public void addChildren(String path, boolean exclude)
    {
        int idx = path.indexOf('.');
        if (idx == -1)
        {
            if ("*".equals(path))
            {
                excludeChildren = exclude;
            }
            else
            {
                PackageTreeNode child = children.get(path);
                if (child == null)
                {
                    child = new PackageTreeNode(path, exclude);
                    children.put(path, child);
                }
                else
                {
                    child.exclude = exclude;
                }
            }
        }
        else
        {
            String name = path.substring(0, idx);
            String remainingPath = path.substring(idx + 1);
            PackageTreeNode child = children.get(name);
            if (child == null)
            {
                child = new PackageTreeNode(name, false);
                children.put(name, child);
            }

            child.addChildren(remainingPath, exclude);
        }
    }


    public boolean shouldExclude(String path)
    {
        int idx = path.indexOf('.');
        if (idx == -1)
        {
            PackageTreeNode child = children.get(path);
            if (child == null)
            {
                return excludeChildren;
            } // no specific behavior for this child - return excludeChildren.
            else
            {
                return child.exclude;
            } // this child has been explicitly listed.
        }
        else
        {
            String name = path.substring(0, idx);
            PackageTreeNode child = children.get(name);
            if (child == null)
            {
                return excludeChildren;
            } // this is the best matching package, return its result.
            else
            {
                String remainingPath = path.substring(idx + 1);
                return child.shouldExclude(remainingPath);
            }// deeper we go!
        }
    }


    public static void main(String[] args)
    {
        String path1 = "com.seeburger.engine.impl.*";
        String path2 = "com.seeburger.engine.impl";
        String path3 = "com.seeburger.engine.impl.fubar";
        String path4 = "com.seeburger.engine.exc";

        PackageTreeNode node = new PackageTreeNode("root", false);
        node.addChildren(path1, true);
        node.addChildren(path2, true);
        node.addChildren(path3, false);
        node.addChildren(path4, true);

        System.out.println(node.shouldExclude("com.seeburger.engine"));
        System.out.println(node.shouldExclude("com.seeburger.engine.impl"));
        System.out.println(node.shouldExclude("com.seeburger.engine.impl.soapbar"));
        System.out.println(node.shouldExclude("com.seeburger.engine.impl.fubar.fubar"));
        System.out.println(node.shouldExclude("test"));
        System.out.println(node.shouldExclude("com.seeburger.engine.exc"));
        System.out.println(node.shouldExclude("com.seeburger.engine.exc.inner"));

        String paths = "com.seeburger.engine.impl,com.seeburger.engine.impl.*, !com.seeburger.engine.impl.fubar, com.seeburger.engine.exc";
        String[] split = paths.split(",");
        for (String s : split)
        {
            String trimmed = s.trim();
            if (trimmed.startsWith("!"))
            {
                trimmed = trimmed.substring(1);
                trimmed = trimmed + " negative";
            }
            System.out.println(trimmed);
        }
    }


    @Override
    public String toString()
    {
        return "PackageTreeNode [exclude=" + exclude + ", name=" + name + "]";
    }
}
