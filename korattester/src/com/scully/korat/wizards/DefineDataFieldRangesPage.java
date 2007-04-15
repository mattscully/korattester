package com.scully.korat.wizards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.scully.korat.Util;
import com.scully.korat.map.StateFieldDTO;

public class DefineDataFieldRangesPage extends KoratWizardPage
{
    private WizTypeInfo wizTypeInfo;

    private Map<String, Control> fieldRangeMinMap = new HashMap<String, Control>();

    private Map<String, Control> fieldRangeMaxMap = new HashMap<String, Control>();

    private Map<String, Button> fieldNullableCheckboxMap = new HashMap<String, Button>();

    private Map<String, Control> fieldArrayMap = new HashMap<String, Control>();

    public DefineDataFieldRangesPage(String pageName, WizTypeInfo wizTypeInfo)
    {
        super(pageName);
        this.wizTypeInfo = wizTypeInfo;
        setTitle("Define Data Field Ranges for State Space");
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
        layout.numColumns = 8;
        layout.verticalSpacing = 9;

        Map<String, List<StateFieldDTO>> dataFields = this.wizTypeInfo.getDataFields();

        for (String parentType : dataFields.keySet())
        {
            // add parent type label
            addLabel(container, parentType, GridData.FILL_HORIZONTAL).horizontalSpan = 8;

            for (StateFieldDTO field : dataFields.get(parentType))
            {
                if ("boolean".equals(field.getType()))
                {
                    createBooleanControls(container, field);
                }
                else if ("int[]".equals(field.getType()) || Util.isTypeSupportedByIntegerArray(field.getType()))
                {
                    createArrayControls(container, field);
                }
                else if (Util.isTypeSupportedByInteger(field.getType()))
                {
                    createObjectControls(container, field);
                }
                else
                {
                    createWholeNumberControls(container, field);
                }
            }
        }

        clearErrorsForFirstPageLoad();
        setControl(container);
    }

    @Override
    protected boolean isValid()
    {
        boolean isValid = true;
        int min = 0;
        int max = 0;
        int size = 0;
        for (List<StateFieldDTO> fields : this.wizTypeInfo.getDataFields().values())
        {
            for (StateFieldDTO field : fields)
            {
                // verify numeric values
                try
                {
                    min = this.getFieldMin(field);
                    max = this.getFieldMax(field);
                    size = this.getFieldSize(field);
                }
                catch (NumberFormatException e)
                {
                    this.addErrorMessage("- All entered values must be numeric.");
                    // return here if not numeric, can't go further
                    return false;
                }
                if("boolean".equals(field.getType()))
                {
                    if(min > max)
                    {
                        this.addErrorMessage("- At least one of True or False must be selected for boolean fields.");
                        isValid = false;
                    }
                }
                else
                {
                    if(min > max)
                    {
                        this.addErrorMessage("- Min values must be less than or equal to Max values.");
                        isValid = false;
                    }
                    if(size < 0)
                    {
                        this.addErrorMessage("- Array sizes cannot be negative.");
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
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
        addLabel(container, "min:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("0");
        GridData gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("3");
        gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalSpan = 4;
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
        addLabel(container, "min:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("0");
        GridData gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("3");
        gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMaxMap.put(getFieldKey(field), text);

        // add nullable checkbox
        Button isNullable = new Button(container, SWT.CHECK);
        isNullable.setText("Nullable");
        isNullable.setSelection(true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
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
        addLabel(container, "min:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("0");
        GridData gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMinMap.put(getFieldKey(field), text);

        // add max
        addLabel(container, "max:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("2");
        gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        gd.widthHint = 30;
        this.fieldRangeMaxMap.put(getFieldKey(field), text);

        boolean isNullableEnabled = Util.isTypeSupportedByIntegerArray(field.getType());

        // add arraySize
        addLabel(container, "size:", GridData.FILL_HORIZONTAL).horizontalAlignment = SWT.RIGHT;
        text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("2");
        gd = addInputField(container, text, true, true);
        gd.horizontalAlignment = SWT.LEFT;
        if (!isNullableEnabled)
        {
            gd.horizontalSpan = 2;
        }
        gd.widthHint = 30;
        this.fieldArrayMap.put(getFieldKey(field), text);

        if (isNullableEnabled)
        {
            // add nullable checkbox
            Button isNullable = new Button(container, SWT.CHECK);
            isNullable.setText("Contains NULLs");
            isNullable.setSelection(true);
            gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalAlignment = SWT.LEFT;
            isNullable.setLayoutData(gd);
            this.fieldNullableCheckboxMap.put(getFieldKey(field), isNullable);
        }
    }

    /**
     * @param container
     * @param field
     */
    private void createBooleanControls(Composite container, StateFieldDTO field)
    {
        // add field name/type label
        addLabel(container, field.getName() + "  (" + field.getType() + ")", GridData.FILL_HORIZONTAL).horizontalSpan = 2;

        // add false
        Button isFalseEnabled = new Button(container, SWT.CHECK);
        isFalseEnabled.setText("false");
        isFalseEnabled.setSelection(true);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        isFalseEnabled.setLayoutData(gd);
        this.addValidationListener(isFalseEnabled);
        this.fieldRangeMinMap.put(getFieldKey(field), isFalseEnabled);

        // add true
        Button isTrueEnabled = new Button(container, SWT.CHECK);
        isTrueEnabled.setText("true");
        isTrueEnabled.setSelection(true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 4;
        isTrueEnabled.setLayoutData(gd);
        this.addValidationListener(isTrueEnabled);
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
}
