package com.scully.korat.popup.actions;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;

import com.scully.korat.KoratPlugin;
import com.scully.korat.wizards.StateSpaceWizard;

public class CreateStateSpace implements IObjectActionDelegate
{

//    IType lastSelection;
    IStructuredSelection lastSelection;

    /**
     * Constructor for Action1.
     */
    public CreateStateSpace()
    {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart)
    {
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action)
    {
//        String msg = "Generate Finitization was executed.";
        if (this.lastSelection != null)
        {
            // popup Wizard Dialog
            StateSpaceWizard wizard = new StateSpaceWizard();
            wizard.init(getWorkbench(), (IStructuredSelection) this.lastSelection);
            WizardDialog dialog = new WizardDialog(getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
            dialog.open();
            //			msg += "\n" + this.lastSelection.getClass();
            //            generateFinitizationClass();
        }
        /*
         * Shell shell = new Shell(); MessageDialog.openInformation( shell,
         * "Korat Plug-in", msg);
         */
    }

    /**
     * @return
     */
    private IWorkbench getWorkbench()
    {
        return KoratPlugin.getDefault().getWorkbench();
    }


    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection ss = (IStructuredSelection) selection;
            if (!ss.isEmpty())
            {
                Object firstObj = ss.getFirstElement();
                if (firstObj instanceof IType)
                {
//                    this.lastSelection = (IType) firstObj;
                    this.lastSelection = ss;
                }
//                else if (firstObj instanceof IFile)
//                {
//                    this.lastSelection = ss;
//                }
                else
                {
                    this.lastSelection = null;
                }
            }
            else
            {
                this.lastSelection = null;
            }
        }
        else
        {
            this.lastSelection = null;
        }
    }

}