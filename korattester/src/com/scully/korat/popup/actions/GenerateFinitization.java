package com.scully.korat.popup.actions;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.scully.korat.popup.actions.model.FinMethod;
import com.scully.korat.popup.actions.model.MainMethod;


public class GenerateFinitization implements IObjectActionDelegate
{

    IMethod lastSelection;

    /**
     * Constructor for Action1.
     */
    public GenerateFinitization()
    {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action)
    {
        String msg = "Generate Finitization was executed.";
        if (this.lastSelection != null)
        {
            //			msg += "\n" + this.lastSelection.getClass();
            generateFinitizationClass();
        }
        /*
         * Shell shell = new Shell(); MessageDialog.openInformation( shell,
         * "Korat Plug-in", msg);
         */
    }

    /**
     *  
     */
    private void generateFinitizationClass()
    {
        // get the encapsulating class name
        IType declaringType = this.lastSelection.getDeclaringType();
        String className = declaringType.getTypeQualifiedName();
        String finClassName = className + "_" + this.lastSelection.getElementName();
        String finFileName = finClassName + ".java";

        try
        {
            IResource resource = declaringType.getUnderlyingResource();
            if (resource instanceof IFile)
            {
                IFile file = (IFile) resource;
                StringBuffer fileSource = new StringBuffer(512);
                fileSource.append(getPackageStatement(declaringType));
                fileSource.append(getImportStatements());
                fileSource.append("public class ").append(finClassName).append(
                        " {\n\n");

                // create fin method
                FinMethod finMethod = new FinMethod(className);
                // recursively add fields for this type and each sub type
                addFieldsForType(declaringType, finMethod);
                
                MainMethod mainMethod = new MainMethod(this.lastSelection, finMethod.getParameters());

                fileSource.append(finMethod.toString());
                fileSource.append(mainMethod.toString()).append("}");
                
                createFinFileInFolder(file.getParent(), finFileName, fileSource.toString());
            }

        }
        catch (JavaModelException e)
        {
            e.printStackTrace();
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    private String getImportStatements()
    {
        StringBuffer buf = new StringBuffer(64);
        buf.append("import com.scully.korat.KoratClient;\n");
        buf.append("import com.scully.korat.finitization.*;\n");
        buf.append("\n");
        return buf.toString();
    }

    /**
     * @param type TODO
     * @param file
     * @return
     */
    private String getPackageStatement(IType type)
    {
        String packageName = type.getPackageFragment().getElementName();
        if("".equals(packageName))
        {
            return "";
        }
        StringBuffer buf = new StringBuffer("package ");
        buf.append( type.getPackageFragment().getElementName());
        buf.append(";\n\n");
        return buf.toString();
    }

    /**
     * @param type
     * @param finMethod
     * @throws JavaModelException
     */
    private void addFieldsForType(IType type, FinMethod finMethod)
            throws JavaModelException
    {
        IField[] fields = type.getFields();
        for (int j = 0; j < fields.length; j++)
        {
            IField field = fields[j];
            finMethod.addField(field);
        }
        addFieldsForTypes(type.getTypes(), finMethod);
    }

    /**
     * @param type
     * @param finMethod
     * @throws JavaModelException
     */
    private void addFieldsForTypes(IType[] types, FinMethod finMethod)
            throws JavaModelException
    {
        if (types == null)
        {
            return;
        }
        for (int i = 0; i < types.length; i++)
        {
            addFieldsForType(types[i], finMethod);
        }

    }

    /**
     * @param container
     * @param file
     * @throws CoreException
     */
    private void createFinFileInFolder(IContainer parent, String fileName,
            String contents) throws CoreException
    {
        IFile newFile = null;
        if(parent instanceof IFolder)
        {
	        newFile = ((IFolder) parent).getFile(fileName);
        }
        else if(parent instanceof IProject)
        {
	        newFile = ((IProject) parent).getFile(fileName);
        }
        else
        {
            System.err.println("Couldn't create file for parent: " + parent.getClass());
            return;
        }
        if (!newFile.exists())
        {
            ByteArrayInputStream source = new ByteArrayInputStream(contents
                    .getBytes());
            newFile.create(source, true, null);
        }
        else
        {
            // TODO: Finitization class already exists!
            System.err.println("File already exists: " + fileName);
            System.err.println("OVERWRITING...");
            ByteArrayInputStream source = new ByteArrayInputStream(contents
                    .getBytes());
            newFile.setContents(source, IFile.FORCE + IFile.KEEP_HISTORY, null);
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection) selection;
            if (!ss.isEmpty())
            {
                Object firstObj = ss.getFirstElement();
                if (firstObj instanceof IMethod)
                {
                    this.lastSelection = (IMethod) firstObj;
                }
                else
                {
                    this.lastSelection = null;
                }
            }
            else
            {
                this.lastSelection = null;
            }
        }
        else
        {
            this.lastSelection = null;
        }
    }

}