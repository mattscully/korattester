package com.scully.korat.wizards;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DefineObjPoolsPage extends WizardPage
{
    private IType selection;
    
    private Set<String> types = new HashSet<String>();
    
	private Map<String, Text> objPoolSizeTextsMap = new HashMap<String, Text>();
    
	private Map<String, Button> objPoolNullableCheckboxMap = new HashMap<String, Button>();

    public DefineObjPoolsPage(IType selection)
    {
        super("objectPoolPage");
        setTitle("Define Object Pools for State Space");
        setDescription("This wizard creates an XML file representing the selected object's state space.");
        this.selection = selection;
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    public void createControl(Composite parent)
    {
        try
        {
            collectTypes(this.selection.getTypes());
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        
        for (String type : types)
        {
	        // add label
	        addLabel(container, type + ":");
            
            Text size = new Text(container, SWT.BORDER | SWT.SINGLE);
            size.setText("3");
            addInputField(container, size, true);
            this.objPoolSizeTextsMap.put(type, size);
            
            Button isNullable = new Button(container, SWT.CHECK);
            isNullable.setText("Nullable");
            isNullable.setSelection(true);
            this.objPoolNullableCheckboxMap.put(type, isNullable);
        }


        //        initialize();
        //        dialogChanged();
        setControl(container);
    }

    /**
     * Recursively get a set of all unique object types within types
     * @param fieldTypes
     * @param typesSet
     * @throws JavaModelException
     */
    private void collectTypes(IType[] fieldTypes) throws JavaModelException
    {
        if (fieldTypes != null)
        {
            for (IType childType : fieldTypes)
            {
                if (!this.types.contains(childType))
                {
                    this.types.add(childType.getFullyQualifiedName());
                    collectTypes(childType.getTypes());
                }
            }
        }
    }

    private Label addLabel(Composite container, String text)
    {
        Label label = new Label(container, SWT.NULL);
        label.setText(text);
        return label;
    }

    private GridData addInputField(Composite container, Text text, boolean enabled)
    {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gd);
        text.setEnabled(enabled);
        return gd;
    }
}
