package cameoExamplePlugin;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.ui.dialogs.SelectElementInfo;
import com.nomagic.magicdraw.ui.dialogs.selection.ElementSelectionDlgFactory;
import com.nomagic.magicdraw.ui.dialogs.selection.TypeFilterImpl;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.*;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;

import java.util.*;

public class CameoBlockSelector {

    public static Optional<Classifier> displayValueChooser() {
        var blockStereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(), "Block");

        var typeFilter = new TypeFilterImpl(Collections.singleton(Class.class))
        {

            @Override
            public boolean accept(BaseElement baseElement, boolean b) {
                if(!super.accept(baseElement, b))
                {
                    return false;
                }
                if(baseElement instanceof Classifier)
                {
                    return ((Classifier)baseElement).getAppliedStereotype().contains(blockStereotype);
                }
                return false;
            }
        };
        var dialog = ElementSelectionDlgFactory.create(MDDialogParentProvider.getProvider().getDialogOwner(null),
                "Select Cameo Block",
                null);
        SelectElementInfo selectElementInfo = new SelectElementInfo(true, false, null, true);
        ElementSelectionDlgFactory.initSingle(dialog,
                selectElementInfo ,
                typeFilter,
                typeFilter,
                true ,
                null,
                null);
        dialog.setVisible(true);
        if (dialog.getResult() == 1) {
            var selectedElement =  dialog.getSelectedElement();
            if(selectedElement instanceof Classifier){
                return Optional.of((Classifier) selectedElement);
            }
        }
        return Optional.empty();
    }
}
