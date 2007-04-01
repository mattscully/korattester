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

import com.scully.korat.Util;
import com.scully.korat.map.StateFieldDTO;

public class DefineNativeFieldRangesPage extends WizardPage
{
    private WizTypeInfo wizTypeInfo;

    private Map<String, Control> fieldRangeMinMap = new HashMap<String, Control>();

    private Map<String, Control> fieldRangeMaxMap = new HashMap<String, Control>();
    
    private Map<String, Button> fieldNullableCheckboxMap = new HashMap<String, Button>();

    private Map<String, Control> fieldArrayMap = new HashMap<String, Control>();

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
        layout.numColumns = 7;
        layout.verticalSpacing = 9;

        Map<String, List<StateFieldDTO>> primitiveFields = this.wizTypeInfo.getPrimitiveFields();

        for (String parentType : primitiveFields.keySet())
        {
            // add parent type label
            addLabel(container, parentType).horizontalSpan = 7;

            for (StateFieldDTO field : primitiveFields.get(parentType))
            {
                if ("boolean".equals(field.getType()))
                {
                    createBooleanControls(container, field);
                }
                else if ("int[]".equals(field.getType()))
                {
                    createArrayControls(container, field);
                }
                else if (Util.isSupportedNonConcreteClass(field.getType()))
                {
                    createObjectControls(container, field);
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
        GridData gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:").horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("3");
        gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalSpan = 3;
        gd.widthHint = 30;
        this.fieldRangeMaxMap.put(getFieldKey(field), text);
    }
    
    /**
     * @param container
     * @param field
     */
    private void createObjectControls(Composite container, StateFieldDTO field)
    {
        // add field name/type label
        addLabel(container, field.getName() + "  (" + field.getType() + ")");

        // add min
        addLabel(container, "min:").horizontalAlignment = SWT.RIGHT;
        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("0");
        GridData gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:").horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("3");
        gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMaxMap.put(getFieldKey(field), text);
        
        // add nullable checkbox
        Button isNullable = new Button(container, SWT.CHECK);
        isNullable.setText("Nullable");
        isNullable.setSelection(true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.horizontalAlignment = SWT.CENTER;
        isNullable.setLayoutData(gd);
        this.fieldNullableCheckboxMap.put(getFieldKey(field), isNullable);
    }

    /**
     * @param container
     * @param field
     */
    private void createArrayControls(Composite container, StateFieldDTO field)
    {
        // add field name/type label
        addLabel(container, field.getName() + "  (" + field.getType() + ")");

        // add min
        addLabel(container, "min:").horizontalAlignment = SWT.RIGHT;
        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("0");
        GridData gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:").horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("2");
        gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMaxMap.put(getFieldKey(field), text);

        // add arraySize
        addLabel(container, "size:").horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("2");
        gd = addInputField(container, text, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldArrayMap.put(getFieldKey(field), text);
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
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        isTrueEnabled.setLayoutData(gd);
        this.fieldRangeMaxMap.put(getFieldKey(field), isTrueEnabled);
    }

    public int getFieldMin(StateFieldDTO field)
    {
        Control control = this.fieldRangeMinMap.get(getFieldKey(field));
        // numeric
        if (control instanceof Text)
        {
            return Integer.parseInt(((Text) control).getText());
        }
        // boolean
        return ((Button) control).getSelection() ? 0 : 1;
    }

    public int getFieldMax(StateFieldDTO field)
    {
        Control control = this.fieldRangeMaxMap.get(getFieldKey(field));
        // numeric
        if (control instanceof Text)
        {
            return Integer.parseInt(((Text) control).getText());
        }
        // boolean
        return ((Button) control).getSelection() ? 1 : 0;
    }

    public int getFieldSize(StateFieldDTO field)
    {
        Control control = this.fieldArrayMap.get(getFieldKey(field));
        // null
        if (control == null)
        {
            return 0;
        }
        // numeric
        else if (control instanceof Text)
        {
            return Integer.parseInt(((Text) control).getText());
        }
        // boolean
        else if (control instanceof Button)
        {
            return ((Button) control).getSelection() ? 1 : 0;
        }
        
        return 0;
    }
    
    public boolean isFieldNullable(StateFieldDTO field)
    {
        Control control = this.fieldNullableCheckboxMap.get(getFieldKey(field));
        if (control == null)
        {
            return false;
        }
        
        return ((Button) control).getSelection();
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
