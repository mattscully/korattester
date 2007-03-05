package com.scully.korat.wizards;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (xml).
 */

public class NewStateSpaceWizPage extends WizardPage
{
    //    private Text containerText;

    private Text baseClassText;

    private Text fileText;

    private Text repOkMethodText;

    private Text sourceFolderText;

    private Text packageNameText;

    private WizTypeInfo wizTypeInfo;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewStateSpaceWizPage(WizTypeInfo wizTypeInfo)
    {
        super("stateSpacePage");
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
        addInputField(container, this.baseClassText, false, false);

        // add Browse button for Base Class
        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e)
            {
                handleBrowse();
            }
        });

        // add File Name
        addLabel(container, "&File name:");
        this.fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.fileText, true, true).horizontalSpan = 2;

        // add RepOk
        addLabel(container, "&RepOk Method:");
        this.repOkMethodText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.repOkMethodText, true, true).horizontalSpan = 2;

        // add Source folder
        addLabel(container, "Source fol&der:");
        this.sourceFolderText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.sourceFolderText, true, true).horizontalSpan = 2;

        // add Package
        addLabel(container, "&Package:");
        this.packageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
        addInputField(container, this.packageNameText, true, true).horizontalSpan = 2;

        initialize();
        dialogChanged();
        setControl(container);
    }

    private Label addLabel(Composite container, String text)
    {
        Label label = new Label(container, SWT.NULL);
        label.setText(text);
        return label;
    }

    private GridData addInputField(Composite container, Text text, boolean enabled, boolean handleModify)
    {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gd);
        text.setEnabled(enabled);
        if (handleModify)
        {
            text.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e)
                {
                    dialogChanged();
                }
            });
        }
        return gd;
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
        this.repOkMethodText.setText("repOk");
        this.sourceFolderText.setText("src");
        this.packageNameText.setText(this.wizTypeInfo.getType().getPackageFragment().getElementName());
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */

    private void handleBrowse()
    {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace()
                .getRoot(), false, "Select new file container");
        if (dialog.open() == ContainerSelectionDialog.OK)
        {
            Object[] result = dialog.getResult();
            if (result.length == 1)
            {
                this.sourceFolderText.setText(((Path) result[0]).toString());
            }
        }
    }

    /**
     * Ensures that both text fields are set.
     */

    private void dialogChanged()
    {
        //        IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
        //        String fileName = getFileName();
        //
        //        if (getContainerName().length() == 0)
        //        {
        //            updateStatus("File container must be specified");
        //            return;
        //        }
        //        if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0)
        //        {
        //            updateStatus("File container must exist");
        //            return;
        //        }
        //        if (!container.isAccessible())
        //        {
        //            updateStatus("Project must be writable");
        //            return;
        //        }
        //        if (fileName.length() == 0)
        //        {
        //            updateStatus("File name must be specified");
        //            return;
        //        }
        //        if (fileName.replace('\\', '/').indexOf('/', 1) > 0)
        //        {
        //            updateStatus("File name must be valid");
        //            return;
        //        }
        //        int dotLoc = fileName.lastIndexOf('.');
        //        if (dotLoc != -1)
        //        {
        //            String ext = fileName.substring(dotLoc + 1);
        //            if (ext.equalsIgnoreCase("xml") == false)
        //            {
        //                updateStatus("File extension must be \"xml\"");
        //                return;
        //            }
        //        }
        updateStatus(null);
    }

    private void updateStatus(String message)
    {
        setErrorMessage(message);
        setPageComplete(message == null);
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
        return packageNameText.getText();
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
    public String getSourceFolder()
    {
        return sourceFolderText.getText();
    }
} //  @jve:decl-index=0:visual-constraint="105,83"