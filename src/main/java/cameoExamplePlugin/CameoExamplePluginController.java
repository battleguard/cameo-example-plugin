package cameoExamplePlugin;

import javax.swing.JOptionPane;

public class CameoExamplePluginController {
    CameoExamplePluginView view;
    CameoExamplePluginModel model;

    public CameoExamplePluginController(CameoExamplePluginView view, CameoExamplePluginModel model)
    {
        this.view = view;
        this.model = model;

        view.serializeCameoBlock.addActionListener(e -> displayProjectOptionTextButton());
        view.printProjectOptionText.addActionListener(e -> printProjectOptionText());
    }

    public void displayProjectOptionTextButton()
    {
        var block = model.chooseCameoBlock();
        if (block.isPresent())
        {
            var diagram = CameoElementConfigParser.CreateCameoDiagramData(block.get());
            var diagramXml = CameoElementConfigParser.toString(diagram);
            var window = BasicFileTextboxWindowDialog.create("Exported Diagram", diagramXml);
            window.setVisible(true);
        }
    }

    public void printProjectOptionText()
    {
        JOptionPane.showMessageDialog(null, model.getDisplayText());
    }
}
