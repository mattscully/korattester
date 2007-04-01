package com.scully.korat.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.NotFoundException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.scully.korat.instrument.Loader;
import com.scully.korat.map.BeanXmlMapper;
import com.scully.korat.map.StateFieldDTO;
import com.scully.korat.map.StateSpaceBuilder;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "xml". If a sample multi-newStateSpacePage editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class StateSpaceWizard extends Wizard implements INewWizard
{
    private NewStateSpacePage newStateSpacePage;

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
     * Adding the newStateSpacePage to the wizard.
     */

    public void addPages()
    {
        this.wizTypeInfo = new WizTypeInfo(this.selection);
        this.newStateSpacePage = new NewStateSpacePage("stateSpacePage", this.wizTypeInfo);
        this.defineObjPoolsPage = new DefineObjPoolsPage("objectPoolPage", this.wizTypeInfo);
        this.defineNativeFieldRangesPage = new DefineNativeFieldRangesPage("primitiveFieldRangesPage", this.wizTypeInfo);
        addPage(this.newStateSpacePage);
        addPage(this.defineObjPoolsPage);
        addPage(this.defineNativeFieldRangesPage);
    }
    
    public void updateState(IType baseType)
    {
        this.selection = baseType;
    }

    /**
     * This method is called when 'Finish' button is pressed in
     * the wizard. We will create an operation and run it
     * using wizard as execution context.
     */
    public boolean performFinish()
    {
        final String containerName = newStateSpacePage.getTargetSourceFolder();
        final String fileName = newStateSpacePage.getFileName();
        final String packageName = newStateSpacePage.getPackageName();
        collectPageData();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException
            {
                try
                {
                    doFinish(containerName, packageName, fileName, monitor);
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

    private void doFinish(String containerName, String packageName, String fileName, IProgressMonitor monitor) throws CoreException
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
        if(StringUtils.isNotBlank(packageName))
        {
            packageName = packageName.replace('.', '/');
	        IFolder packageFolder = container.getFolder(new Path(packageName));
	        if(!packageFolder.exists())
	        {
		        packageFolder.create(false, true, null);
	        }
            container = packageFolder;
        }
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
            e.printStackTrace();
        }
        monitor.worked(1);
//        monitor.setTaskName("Opening file for editing...");
//        getShell().getDisplay().asyncExec(new Runnable() {
//            public void run()
//            {
//                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//                try
//                {
//                    IDE.openEditor(page, file, true);
//                }
//                catch (PartInitException e)
//                {
//                }
//            }
//        });
//        monitor.worked(1);
    }

    private void collectPageData()
    {
        // set the root
        this.stateSpaceBuilder.setRootClass(this.newStateSpacePage.getBaseClass());

        // ==> create StateObjects
        for (String usedType : this.wizTypeInfo.getUsedTypes())
        {
            this.stateSpaceBuilder.addStateObject(usedType, this.defineObjPoolsPage.getObjectPoolSize(usedType),
                    this.defineObjPoolsPage.isNullable(usedType));
        }

        // ==> create StateFields
        // add primitive fields
        for (List<StateFieldDTO> fields : this.wizTypeInfo.getPrimitiveFields().values())
        {
            for (StateFieldDTO field : fields)
            {
                field.setMin(this.defineNativeFieldRangesPage.getFieldMin(field));
                field.setMax(this.defineNativeFieldRangesPage.getFieldMax(field));
                field.setArraySize(this.defineNativeFieldRangesPage.getFieldSize(field));
                field.setNullable(this.defineNativeFieldRangesPage.isFieldNullable(field));
                this.stateSpaceBuilder.addStateField(field);
            }
        }
        // add object fields
        for (List<StateFieldDTO> fields : this.wizTypeInfo.getObjectFields().values())
        {
            for (StateFieldDTO field : fields)
            {
                this.stateSpaceBuilder.addStateField(field);
            }
        }

        // ==> set repOk
        this.stateSpaceBuilder.setRepOk(this.newStateSpacePage.getRepOkMethod());
    }

    /**
     * We will initialize file contents with a sample text.
     */
    private InputStream openContentStream()
    {
        String contents = getStateSpaceXmlUsingClassLoader();
//        getStateSpaceXmlUsingLauncher();
        return new ByteArrayInputStream(contents.getBytes());
    }

    // added org.eclipse.debug.core and org.eclipse.jdt.launching to plugin.xml
    private void getStateSpaceXmlUsingLauncher()
    {
//        String contents = null;
        IVMInstall vmInstall = null;
        List<String> koratClassPath = getClasspath();
        try
        {
            vmInstall = JavaRuntime.getVMInstall(this.selection.getJavaProject());
        }
        catch (CoreException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (vmInstall == null)
            vmInstall = JavaRuntime.getDefaultVMInstall();
        if (vmInstall != null)
        {
            IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
            if (vmRunner != null)
            {
                try
                {
	                String[] classPath = JavaRuntime.computeDefaultRuntimeClassPath(this.selection.getJavaProject());
                    koratClassPath.addAll(Arrays.asList(classPath));
                }
                catch (CoreException e)
                {
                    e.printStackTrace();
                }
                if (!koratClassPath.isEmpty())
                {
                    VMRunnerConfiguration vmConfig = new VMRunnerConfiguration("com.scully.korat.KoratMain", (String[]) koratClassPath.toArray(new String[koratClassPath.size()]));
                    ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
                    try
                    {
                        vmRunner.run(vmConfig, launch, null);
                    }
                    catch (CoreException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @return
     */
    private String getStateSpaceXmlUsingClassLoader()
    {
        String contents = null;
        try
        {
            List<String> classpath = getClasspath();
            ClassPool classPool = new ClassPool(true);
            try
            {
                for (String cpEntry : classpath)
                {
                    classPool.appendClassPath(cpEntry);
                }
//                classPool.appendClassPath("C:/Documents and Settings/mscully/My Documents/UT/Archive/Verification and Validation/workspace/korattester/classes");
                classPool.appendClassPath(new ClassClassPath(this.getClass()));
                classPool.childFirstLookup = true;
            }
            catch (NotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            System.out.println("var names: " + ArrayUtils.toString(JavaCore.getClasspathVariableNames()));
//            System.out.println("classpath: " + classpath);
//            System.out.println("classpath: " + classPool);
            // perhaps classes are getting loaded in this parent loader???
            //            Loader loader = new Loader(this.getClass().getClassLoader(), new ClassPool(true));
            Loader loader = new Loader(this.getClass().getClassLoader(), classPool);
            try
            {
                String stateSpaceXml = BeanXmlMapper.beanToXml(this.stateSpaceBuilder.getStateSpace());
                contents = (String) loader.invokeExactMethod("com.scully.korat.KoratMain", "run", new Object[] {
                        stateSpaceXml, classpath.toArray(new String[] {}) });
                //                contents = (String) loader.invokeExactMethod("com.scully.korat.KoratMain", "run",
                //                        new Object[] { stateSpaceXml } );
            }
            catch (Throwable e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //            loader.printClasses(System.out);
            loader = null;
            System.gc();
            System.gc();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return contents;
    }

    /**
     * Returns a list of fully qualified classpath entries needed to run Korat
     * @return
     */
    private List<String> getClasspath()
    {
        List<String> classpath = new ArrayList<String>();
        try
        {
            IPath workspaceLocation = this.selection.getJavaProject().getProject().getWorkspace().getRoot()
                    .getLocation();
            //
            String fullPath = null;
            // TODO: determine which classpath entries are needed for running from plugin
//			for (IClasspathEntry entry : cp)
//            {
//                IPath path = entry.getPath();
//                fullPath = getFullPath(workspaceLocation, path);
//                if (fullPath != null)
//                {
//                    classpath.add(fullPath);
//                }
//            }
            IPath path = this.selection.getJavaProject().getOutputLocation();
            fullPath = getFullPath(workspaceLocation, path);
            if (fullPath != null)
            {
                classpath.add(fullPath);
            }
//            classPool.appendClassPath("C:/Documents and Settings/mscully/My Documents/UT/Archive/Verification and Validation/workspace/korattester/classes");
//            classpath.add(new ClassClassPath(this.getClass()));
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return classpath;
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