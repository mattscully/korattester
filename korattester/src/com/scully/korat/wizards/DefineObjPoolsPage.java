package com.scully.korat.wizards;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DefineObjPoolsPage extends KoratWizardPage
{
    private WizTypeInfo wizTypeInfo;
    
	private Map<String, Text> objPoolSizeTextsMap = new HashMap<String, Text>();
    
	private Map<String, Button> objPoolNullableCheckboxMap = new HashMap<String, Button>();

    public DefineObjPoolsPage(String pageName, WizTypeInfo wizTypeInfo)
    {
        super(pageName);
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
            addInputField(container, size, true, true);
            this.objPoolSizeTextsMap.put(type, size);
            
            Button isNullable = new Button(container, SWT.CHECK);
            isNullable.setText("Nullable");
            isNullable.setSelection(true);
            this.objPoolNullableCheckboxMap.put(type, isNullable);
        }


        clearErrorsForFirstPageLoad();
        setControl(container);
    }
    
    @Override
    protected boolean isValid()
    {
        for(String key : this.objPoolSizeTextsMap.keySet())
        {
            String value = this.objPoolSizeTextsMap.get(key).getText();
            if(NumberUtils.toInt(value, -1) < 0)
            {
                addErrorMessage("- Object pool sizes must be integer values >= 0");
                return false;
            }
        }
        return true;
    }
    
    public int getObjectPoolSize(String objectName)
    {
        return Integer.parseInt(this.objPoolSizeTextsMap.get(objectName).getText());
    }
    
    public boolean isNullable(String objectName)
    {
        return this.objPoolNullableCheckboxMap.get(objectName).getSelection();
    }
}
