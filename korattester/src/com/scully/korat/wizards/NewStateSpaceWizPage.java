package com.scully.korat.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
    private Text containerText;

    private Text fileText;
    
    private Text baseClassText;
    
    private Text repOkMethodText;
    
    private Text packageNameText;
    
    private Text fileNameText;

    private IType selection;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public NewStateSpaceWizPage(IType selection)
    {
        super("objectPoolPage");
        setTitle("Create State Space for " + selection.getFullyQualifiedName());
        setDescription("This wizard creates an XML file representing the selected object's state space.");
        this.selection = selection;
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
        Label label = new Label(container, SWT.NULL);
        label.setText("&Base Class:");

        this.baseClassText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        this.baseClassText.setLayoutData(gd);
        this.baseClassText.setEnabled(false);
//        this.baseClassText.addModifyListener(new ModifyListener() {
//            public void modifyText(ModifyEvent e)
//            {
//                dialogChanged();
//            }
//        });

        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e)
            {
                handleBrowse();
            }
        });
        label = new Label(container, SWT.NULL);
        label.setText("&File name:");

        fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gd);
        fileText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
    }

    public String getBaseClass()
    {
        return this.baseClassText.getText();
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
        this.baseClassText.setText(this.selection.getFullyQualifiedName());
        this.fileText.setText(this.selection.getElementName() + ".xml");
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
                containerText.setText(((Path) result[0]).toString());
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

    public String getContainerName()
    {
        return containerText.getText();
    }

    public String getFileName()
    {
        return fileText.getText();
    }
}  //  @jve:decl-index=0:visual-constraint="105,83"