package com.scully.korat.wizards;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private WizTypeInfo wizTypeInfo;
    
	private Map<String, Text> objPoolSizeTextsMap = new HashMap<String, Text>();
    
	private Map<String, Button> objPoolNullableCheckboxMap = new HashMap<String, Button>();

    public DefineObjPoolsPage(WizTypeInfo wizTypeInfo)
    {
        super("objectPoolPage");
        this.wizTypeInfo = wizTypeInfo;
        setTitle("Define Object Pools for State Space");
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
        
        Set<String> types = this.wizTypeInfo.getUsedTypes();
        
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
    
    public int getObjectPoolSize(String objectName)
    {
        return Integer.parseInt(this.objPoolSizeTextsMap.get(objectName).getText());
    }
    
    public boolean isNullable(String objectName)
    {
        return this.objPoolNullableCheckboxMap.get(objectName).getSelection();
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
