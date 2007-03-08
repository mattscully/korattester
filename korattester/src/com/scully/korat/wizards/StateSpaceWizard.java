package com.scully.korat.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.scully.korat.KoratClient;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateSpaceBuilder;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "xml". If a sample multi-newStateSpaceWizPage editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class StateSpaceWizard extends Wizard implements INewWizard
{
    private NewStateSpaceWizPage newStateSpaceWizPage;

    private DefineObjPoolsPage defineObjPoolsPage;

    private DefineNativeFieldRangesPage defineNativeFieldRangesPage;

    private StateSpaceBuilder stateSpaceBuilder;

    private IType selection;

    private WizTypeInfo wizTypeInfo;

    /**
     * Constructor for StateSpaceWizard.
     */
    public StateSpaceWizard()
    {
        super();
        setNeedsProgressMonitor(true);
        this.stateSpaceBuilder = new StateSpaceBuilder();
    }

    /**
     * Adding the newStateSpaceWizPage to the wizard.
     */

    public void addPages()
    {
        this.wizTypeInfo = new WizTypeInfo(this.selection);
        this.newStateSpaceWizPage = new NewStateSpaceWizPage(this.wizTypeInfo);
        this.defineObjPoolsPage = new DefineObjPoolsPage(this.wizTypeInfo);
        this.defineNativeFieldRangesPage = new DefineNativeFieldRangesPage(this.wizTypeInfo);
        addPage(this.newStateSpaceWizPage);
        addPage(this.defineObjPoolsPage);
        addPage(this.defineNativeFieldRangesPage);
    }

    /**
     * This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it
     * using wizard as execution context.
     */
    public boolean performFinish()
    {
        final String containerName = newStateSpaceWizPage.getSourceFolder();
        final String fileName = newStateSpaceWizPage.getFileName();
        collectPageData();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException
            {
                try
                {
                    doFinish(containerName, fileName, monitor);
                }
                catch (CoreException e)
                {
                    throw new InvocationTargetException(e);
                }
                finally
                {
                    monitor.done();
                }
            }
        };
        try
        {
            getContainer().run(true, false, op);
        }
        catch (InterruptedException e)
        {
            return false;
        }
        catch (InvocationTargetException e)
        {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    /**
     * The worker method. It will find the container, create the
     * file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */

    private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException
    {
        // create a sample file
        monitor.beginTask("Creating " + fileName, 2);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(containerName));
        if (!resource.exists() || !(resource instanceof IContainer))
        {
            throwCoreException("Container \"" + containerName + "\" does not exist.");
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        try
        {
            InputStream stream = openContentStream();
            if (file.exists())
            {
                file.setContents(stream, true, true, monitor);
            }
            else
            {
                file.create(stream, true, monitor);
            }
            stream.close();
        }
        catch (IOException e)
        {
        }
        monitor.worked(1);
        monitor.setTaskName("Opening file for editing...");
        getShell().getDisplay().asyncExec(new Runnable() {
            public void run()
            {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try
                {
                    IDE.openEditor(page, file, true);
                }
                catch (PartInitException e)
                {
                }
            }
        });
        monitor.worked(1);
    }

    private void collectPageData()
    {
        // set the root
        this.stateSpaceBuilder.setRootClass(this.newStateSpaceWizPage.getBaseClass());

        // ==> create StateObjects
        for (String usedType : this.wizTypeInfo.getUsedTypes())
        {
            this.stateSpaceBuilder.addStateObject(usedType, this.defineObjPoolsPage.getObjectPoolSize(usedType),
                    this.defineObjPoolsPage.isNullable(usedType));
        }

        // ==> create StateFields
        for (List<StateFieldDTO> fields : this.wizTypeInfo.getPrimitiveFields().values())
        {
            for (StateFieldDTO field : fields)
            {
                field.setMin(this.defineNativeFieldRangesPage.getFieldMin(field));
                field.setMax(this.defineNativeFieldRangesPage.getFieldMax(field));
                this.stateSpaceBuilder.addStateField(field);
            }
        }

        // ==> set repOk
        this.stateSpaceBuilder.setRepOk(this.newStateSpaceWizPage.getRepOkMethod());
    }

    /**
     * We will initialize file contents with a sample text.
     */
    private InputStream openContentStream()
    {
        try
        {
            List<String> classpath = new ArrayList<String>();
            try
            {
//                IClasspathEntry[] cp = this.selection.getJavaProject().getResolvedClasspath(true);
//                //	            String prefix = this.selection.getJavaProject()
                IPath workspaceLocation = this.selection.getJavaProject().getProject().getWorkspace().getRoot()
                        .getLocation();
//
                String fullPath = null;
//                for (IClasspathEntry entry : cp)
//                {
//                    IPath path = entry.getPath();
//                    fullPath = getFullPath(workspaceLocation, path);
//                    if (fullPath != null)
//                    {
//                        classpath.add(fullPath);
//                    }
//                }
                IPath path = this.selection.getJavaProject().getOutputLocation();
                fullPath = getFullPath(workspaceLocation, path);
                if (fullPath != null)
                    classpath.add(fullPath);
            }
            catch (JavaModelException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String[] codeClasspath = null;
            if (!classpath.isEmpty())
            {
                codeClasspath = classpath.toArray(new String[] {});
            }

            KoratClient.populateTestCandidates(this.stateSpaceBuilder.getStateSpace(), codeClasspath);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        String contents = BeanXmlMapper.beanToXml(this.stateSpaceBuilder.getStateSpace());
        return new ByteArrayInputStream(contents.getBytes());
    }

    private String getFullPath(IPath workspaceLoc, IPath path)
    {
        String fullPath = null;
        if (path != null)
        {
            if (path.getDevice() != null || workspaceLoc.isPrefixOf(path))
            {
                fullPath = path.makeAbsolute().toString();
            }
            else
            {
                fullPath = workspaceLoc.toString() + path.makeAbsolute().toString();
            }
        }
        return fullPath;
    }

    private void throwCoreException(String message) throws CoreException
    {
        IStatus status = new Status(IStatus.ERROR, "com.scully.korat", IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if
     * we can initialize from it.
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        Object firstObj = selection.getFirstElement();
        if (firstObj instanceof IType)
        {
            this.selection = (IType) firstObj;
        }
    }
}