package com.scully.korat.popup.actions;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.osgi.framework.Bundle;

import com.scully.korat.KoratPlugin;

public class AddKoratToClasspathAction implements IObjectActionDelegate
{

    private IJavaElement lastSelection;

    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
        // TODO Auto-generated method stub

    }

    public void run(IAction action)
    {
        // TODO Auto-generated method stub
        // here I'm trying to get the plugin's installation directory.
        String[] requiredJars = new String[] { "/lib/commons-beanutils.jar", "/lib/commons-betwixt-0.8.jar",
                "/lib/commons-collections-3.2.jar", "/lib/commons-digester-1.7.jar", "/lib/commons-lang-2.1.jar",
                "/lib/commons-logging-1.1.jar", "/lib/javassist-3.4.jar" };

        if (this.lastSelection != null)
        {
            IJavaProject javaProject = this.lastSelection.getJavaProject();

            try
            {
                IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
                Bundle bundle = KoratPlugin.getDefault().getBundle();
                for (int i = 0; i < requiredJars.length; i++)
                {
                    String jar = requiredJars[i];
                    URL jarUrl = bundle.getEntry(jar);
//                    System.out.println("URL Path: " + jarUrl.getPath());
                    IPath path = new Path(jarUrl.getPath());
//                    System.out.println("Path: " + path);
//                    System.out.println("Path OS: " + path.toOSString());
//                    System.out.println("Path Portable: " + path.toPortableString());
                    IClasspathEntry entry = JavaCore.newLibraryEntry(path, null, null);
                    if (!ArrayUtils.contains(rawClasspath, entry))
                    {
                        rawClasspath = (IClasspathEntry[]) ArrayUtils.add(rawClasspath, entry);
                    }
                }

                javaProject.setRawClasspath(rawClasspath, null);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        this.lastSelection = null;
//        System.out.println("New Selection: " + selection);
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection) selection;
            if (!ss.isEmpty())
            {
                Object firstObj = ss.getFirstElement();
//		        System.out.println("isJavaElement: " + (firstObj instanceof IJavaElement));
                if (firstObj instanceof IJavaElement)
                {
                    this.lastSelection = (IJavaElement) firstObj;
                }
            }
        }
    }

}
