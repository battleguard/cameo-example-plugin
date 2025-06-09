package cameoExamplePlugin;

import com.nomagic.magicdraw.ui.ProjectWindowsManager;
import com.nomagic.magicdraw.ui.WindowComponentInfo;
import com.nomagic.magicdraw.ui.browser.WindowComponent;
import com.nomagic.magicdraw.ui.browser.WindowComponentContent;
import com.nomagic.ui.ExtendedPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CameoExamplePluginView extends ExtendedPanel implements WindowComponent {
    public static final WindowComponentInfo info = new WindowComponentInfo("cameoExamplePlugin",
            "Cameo Example Plugin",  null,  //icon
            ProjectWindowsManager.SIDE_WEST,
            ProjectWindowsManager.STATE_DOCKED,
            true);

    public CameoExamplePluginModel model;

    public final  JButton serializeCameoBlock = new JButton("Serialize Cameo Block");
    public final  JButton printProjectOptionText = new JButton("Print Project Option Text");
    public final JPanel buttonPanel = new JPanel(new GridLayout(0,2));

    public final JPanel statusPanel = new JPanel();
    private ColorIcon statusIcon;
    private JLabel statusLabel = new JLabel("Example Plugin Server is offline");

    public CameoExamplePluginView(CameoExamplePluginModel model)
    {
        this.model = model;

        buttonPanel.add(serializeCameoBlock);
        buttonPanel.add(printProjectOptionText);
        initializeStatusBar();

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void initializeStatusBar()
    {
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setLayout(new BorderLayout());
        statusIcon = new ColorIcon(12, Color.red);
        statusLabel.setIcon(statusIcon);
        statusLabel.setBorder(new EmptyBorder(2,10,2,10));
        statusPanel.add(statusLabel, BorderLayout.EAST);
    }


    @Override
    public WindowComponentInfo getInfo() {
        return info;
    }

    @Override
    public WindowComponentContent getContent() {
        return new BrowserWindowComponentContext(this);
    }

    private static class BrowserWindowComponentContext implements WindowComponentContent
    {
        final private JPanel panel;

        public BrowserWindowComponentContext(JPanel panel)
        {
            this.panel = panel;
        }

        @Override
        public Component getWindowComponent()
        {
            return panel;
        }

        @Override
        public Component getDefaultFocusComponent()
        {
            return panel;
        }
    }

    private static class ColorIcon implements Icon {

        private final int size;
        private Color color;

        public ColorIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillOval(x, y, size, size);
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}


