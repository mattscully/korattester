package com.scully.korat.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

import com.scully.korat.Util;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (xml).
 */

public class NewStateSpacePage extends KoratWizardPage
{
    //    private Text containerText;

    private Text baseClassText;

    Text fileText;

    Text repOkMethodText;

    Text targetSourceFolderText;

    private String packageName;

    private WizTypeInfo wizTypeInfo;
    
    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewStateSpacePage(String pageName, WizTypeInfo wizTypeInfo)
    {
        super(pageName);
        this.wizTypeInfo = wizTypeInfo;
        setTitle("Create State Space for " + this.wizTypeInfo.getType().getFullyQualifiedName());
        setDescription("This wizard creates an XML file representing the selected object's state space.");
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;

        // add Base Class label
        addLabel(container, "&Base Class:");

        // add Base Class input field
        this.baseClassText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.baseClassText, false, false).horizontalSpan = 2;

        // add File Name
        addLabel(container, "&File name:");
        this.fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.fileText, true, true).horizontalSpan = 2;

        // add Source folder
        addLabel(container, "Target source fol&der:");
        this.targetSourceFolderText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.targetSourceFolderText, false, true);
        
        // add Browse button for source folder
        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e)
            {
                handleSelectTargetSource();
            }
        });

        // add RepOk
        addLabel(container, "&RepOk Method:");
        this.repOkMethodText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.repOkMethodText, false, true);

        // add Browse button for RepOk method
        button = new Button(container, SWT.PUSH);
        button.setText("Method...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e)
            {
                handleSelectMethod();
            }
        });


        // add Package
//        addLabel(container, "&Package:");
//        this.packageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
//        addInputField(container, this.packageNameText, true, true).horizontalSpan = 2;

        initialize();
        setControl(container);
    }

    @Override
    protected boolean isValid()
    {
        boolean isValid = true;
        if(StringUtils.isBlank(this.fileText.getText()))
        {
            addErrorMessage("- File name cannot be blank.");
            isValid &= false;
        }
        if(StringUtils.isBlank(this.targetSourceFolderText.getText()))
        {
            addErrorMessage("- A target source folder must be selected.");
            isValid &= false;
        }
        if(StringUtils.isBlank(this.repOkMethodText.getText()))
        {
            addErrorMessage("- A repOk() method must be selected.");
            isValid &= false;
        }
        return isValid;
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */

    private void initialize()
    {
        //        if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection)
        //        {
        //            IStructuredSelection ssel = (IStructuredSelection) selection;
        //            if (ssel.size() > 1)
        //                return;
        //            Object obj = ssel.getFirstElement();
        //            if (obj instanceof IResource)
        //            {
        //                IContainer container;
        //                if (obj instanceof IContainer)
        //                    container = (IContainer) obj;
        //                else
        //                    container = ((IResource) obj).getParent();
        //                containerText.setText(container.getFullPath().toString());
        //            }
        //        }
        this.baseClassText.setText(this.wizTypeInfo.getType().getFullyQualifiedName());
        this.fileText.setText(this.wizTypeInfo.getType().getElementName() + ".xml");
        IMethod repOk = this.wizTypeInfo.getType().getMethod(Util.REP_OK, null);
        if(repOk.exists())
        {
	        this.repOkMethodText.setText(Util.REP_OK);
        }
        try
        {
            List<String> sourceFolderNames = getSourceFolderNames();
            if(!sourceFolderNames.isEmpty())
            {
		        this.targetSourceFolderText.setText(sourceFolderNames.get(0));
            }
        }
        catch (JavaModelException e)
        {
            e.printStackTrace();
        }
        if(this.wizTypeInfo.getType().getPackageFragment() != null)
        {
	        this.packageName = this.wizTypeInfo.getType().getPackageFragment().getElementName();
        }
        
        this.clearErrorsWhenPageFirstLoaded();
    }

    public IType selectType() throws JavaModelException
    {
        SelectionDialog dialog = JavaUI.createTypeDialog(this.getShell(), new ProgressMonitorDialog(this.getShell()),
                SearchEngine.createWorkspaceScope(), IJavaElementSearchConstants.CONSIDER_ALL_TYPES, false);
        dialog.setTitle("Select Type");
        dialog.setMessage("Select a type");
        if (dialog.open() == IDialogConstants.CANCEL_ID)
            return null;

        Object[] types = dialog.getResult();
        if (types == null || types.length == 0)
            return null;
        return (IType) types[0];
    }

    public String selectMethod() throws JavaModelException
    {
        List<IMethod> methods = Arrays.asList(this.wizTypeInfo.getType().getMethods());
        List<String> methodNames = new ArrayList<String>();
        
        // only use methods that return boolean and don't have any parameters
        for (Iterator<IMethod> iter = methods.iterator(); iter.hasNext();)
        {
            IMethod method = (IMethod) iter.next();
            String[] params = method.getParameterNames();
            String returnType = method.getReturnType();
            if(params.length == 0 && "Z".equals(returnType))
            {
                methodNames.add(method.getElementName());
            }
        }
        
        // Create the list dialog
        ListDialog dialog = new ListDialog(this.getShell());
        dialog.setAddCancelButton(true);
        dialog.setContentProvider(new ArrayContentProvider());
        dialog.setLabelProvider(new LabelProvider());
        dialog.setInput(methodNames);
        dialog.setInitialSelections(new Object[] { this.repOkMethodText.getText() });
        dialog.setTitle("Dialog Title");
        dialog.setMessage("Dialog Message");
        dialog.setHelpAvailable(false);
        if (dialog.open() == IDialogConstants.CANCEL_ID)
            return null;

        Object[] selectedMethods = dialog.getResult();
        if (selectedMethods == null || selectedMethods.length == 0)
            return null;
        return (String) selectedMethods[0];
    }

    public String selectTargetSource() throws JavaModelException
    {
        List<String> sourceFolderNames = getSourceFolderNames();
        // Create the list dialog
        ListDialog dialog = new ListDialog(this.getShell());
        dialog.setAddCancelButton(true);
        dialog.setContentProvider(new ArrayContentProvider());
        dialog.setLabelProvider(new LabelProvider());
        dialog.setInput(sourceFolderNames);
        dialog.setInitialSelections(new Object[] { this.targetSourceFolderText.getText() });
        dialog.setTitle("Select Target Source Folder");
        dialog.setMessage("Select the target source folder where the state space\nfile will be created.  If you have a separate source\nfolder for unit tests, this may be the preferred location.");
        dialog.setHelpAvailable(false);
        if (dialog.open() == IDialogConstants.CANCEL_ID)
            return null;

        Object[] selectedTargetSource = dialog.getResult();
        if (selectedTargetSource == null || selectedTargetSource.length == 0)
            return null;
        return (String) selectedTargetSource[0];
        
    }

    /**
     * @return
     * @throws JavaModelException
     */
    private List<String> getSourceFolderNames() throws JavaModelException
    {
        IJavaProject project = this.wizTypeInfo.getType().getJavaProject();
        List<IPackageFragmentRoot> roots = Arrays.asList(project.getPackageFragmentRoots());
        List<String> sourceFolderNames = new ArrayList<String>();
        for (Iterator iter = roots.iterator(); iter.hasNext();)
        {
            IPackageFragmentRoot root = (IPackageFragmentRoot) iter.next();
            if(!root.isExternal() && root.getKind() == IPackageFragmentRoot.K_SOURCE)
            {
                sourceFolderNames.add(root.getPath().toPortableString());
            }
        }
        return sourceFolderNames;
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */
    private void handleSelectMethod()
    {
        try
        {
            String method = selectMethod();
            if(method != null)
            {
	            this.repOkMethodText.setText(method);
            }
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void handleSelectTargetSource()
    {
        try
        {
            String source = selectTargetSource();
            if(source != null)
            {
	            this.targetSourceFolderText.setText(source);
            }
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getFileName()
    {
        return fileText.getText();
    }

    /**
     * @return the baseClass
     */
    public String getBaseClass()
    {
        return baseClassText.getText();
    }

    /**
     * @return the file
     */
    public String getFile()
    {
        return fileText.getText();
    }

    /**
     * @return the packageName
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * @return the repOkMethod
     */
    public String getRepOkMethod()
    {
        return repOkMethodText.getText();
    }

    /**
     * @return the sourceFolder
     */
    public String getTargetSourceFolder()
    {
        return targetSourceFolderText.getText();
    }
} //  @jve:decl-index=0:visual-constraint="105,83"