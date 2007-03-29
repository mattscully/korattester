package com.scully.korat.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.scully.korat.map.StateFieldDTO;

public class DefineNativeFieldRangesPage extends WizardPage
{
    private WizTypeInfo wizTypeInfo;

    private Map<String, Control> fieldRangeMinMap = new HashMap<String, Control>();

    private Map<String, Control> fieldRangeMaxMap = new HashMap<String, Control>();

    public DefineNativeFieldRangesPage(String pageName, WizTypeInfo wizTypeInfo)
    {
        super(pageName);
        this.wizTypeInfo = wizTypeInfo;
        setTitle("Define Primitive Field Ranges for State Space");
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
        layout.numColumns = 5;
        layout.verticalSpacing = 9;

        Map<String, List<StateFieldDTO>> primitiveFields = this.wizTypeInfo.getPrimitiveFields();

        for (String parentType : primitiveFields.keySet())
        {
            // add parent type label
            addLabel(container, parentType).horizontalSpan = 5;

            for (StateFieldDTO field : primitiveFields.get(parentType))
            {
                if ("boolean".equals(field.getType()))
                {
                    createBooleanControls(container, field);
                }
                else
                {
	                createWholeNumberControls(container, field);
                }
            }
        }

        //        initialize();
        //        dialogChanged();
        setControl(container);
    }

    /**
     * @param container
     * @param field
     */
    private void createWholeNumberControls(Composite container, StateFieldDTO field)
    {
        // add field name/type label
        addLabel(container, field.getName() + "  (" + field.getType() + ")");

        // add min
        addLabel(container, "min:").horizontalAlignment = SWT.RIGHT;
        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("0");
        addInputField(container, text, true);
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:").horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("3");
        addInputField(container, text, true);
        this.fieldRangeMaxMap.put(getFieldKey(field), text);
    }

    /**
     * @param container
     * @param field
     */
    private void createBooleanControls(Composite container, StateFieldDTO field)
    {
        // add field name/type label
        addLabel(container, field.getName() + "  (" + field.getType() + ")").horizontalSpan = 2;

        // add false
        Button isFalseEnabled = new Button(container, SWT.CHECK);
        isFalseEnabled.setText("false");
        isFalseEnabled.setSelection(true);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        isFalseEnabled.setLayoutData(gd);
        
        this.fieldRangeMinMap.put(getFieldKey(field), isFalseEnabled);

        // add true
        Button isTrueEnabled = new Button(container, SWT.CHECK);
        isTrueEnabled.setText("true");
        isTrueEnabled.setSelection(true);
        this.fieldRangeMaxMap.put(getFieldKey(field), isTrueEnabled);
    }

    public int getFieldMin(StateFieldDTO field)
    {
        Control control = this.fieldRangeMinMap.get(getFieldKey(field));
        // numeric
        if(control instanceof Text)
        {
	        return Integer.parseInt(((Text)control).getText());
        }
        // boolean
        return ((Button) control).getSelection() ? 0 : 1;
    }

    public int getFieldMax(StateFieldDTO field)
    {
        Control control = this.fieldRangeMaxMap.get(getFieldKey(field));
        // numeric
        if(control instanceof Text)
        {
	        return Integer.parseInt(((Text)control).getText());
        }
        // boolean
        return ((Button) control).getSelection() ? 1 : 0;
    }

    private String getFieldKey(StateFieldDTO field)
    {
        return field.getParentClass() + "." + field.getName();
    }

    private GridData addLabel(Composite container, String text)
    {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        Label label = new Label(container, SWT.NULL);
        label.setText(text);
        label.setLayoutData(gd);
        return gd;
    }

    private GridData addInputField(Composite container, Text text, boolean enabled)
    {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gd);
        text.setEnabled(enabled);
        return gd;
    }
}
