package com.scully.korat.wizards;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class KoratWizardPage extends WizardPage
{

    private List<String> errorMessages = new ArrayList<String>();

    public KoratWizardPage(String pageName)
    {
        super(pageName);
    }

    protected GridData addLabel(Composite container, String text, int gridDataStyle)
    {
        GridData gd = new GridData(gridDataStyle);
        Label label = addLabel(container, text);
        label.setLayoutData(gd);
        return gd;
    }
    protected Label addLabel(Composite container, String text)
    {
        Label label = new Label(container, SWT.NULL);
        label.setText(text);
        return label;
    }

    protected GridData addInputField(Composite container, Text text, boolean editable, boolean validateOnModify)
    {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gd);
        text.setEditable(editable);
        if (validateOnModify)
        {
            addValidationListener(text);
        }
        return gd;
    }

    /**
     * @param text
     */
    protected void addValidationListener(Text text)
    {
        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e)
            {
                validate();
            }
        });
    }

    /**
     * @param button
     */
    protected void addValidationListener(Button button)
    {
        button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                validate();
            };
            public void widgetDefaultSelected(SelectionEvent e)
            {
                // not needed for me
            }
        });
    }

    protected boolean isValid()
    {
        return true;
    }

    protected void addErrorMessage(String message)
    {
        this.errorMessages.add(message);
    }
    
    private void clearErrors()
    {
        this.errorMessages.clear();
        this.setErrorMessage(null);
    }

    /**
     * Checks validation when fields changed.
     */
    private void validate()
    {
        this.errorMessages.clear();
        if (isValid())
        {
            setErrorMessage(null);
            setPageComplete(true);
        }
        else
        {
            StringBuilder messages = new StringBuilder();
            boolean first = true;
            for (String message : this.errorMessages)
            {
                if (!first)
                {
                    messages.append(SystemUtils.LINE_SEPARATOR);
                }
                messages.append(message);
                first = false;
            }
            setErrorMessage(messages.toString());
            setPageComplete(false);
        }
    }

    protected void clearErrorsWhenPageFirstLoaded()
    {
        if(!isValid())
        {
            // set page complete to false
            this.setPageComplete(false);
            // clear error messages for first load
            this.clearErrors();
        }
    }

}