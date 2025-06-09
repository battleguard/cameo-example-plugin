package cameoExamplePlugin;

import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.ui.MDFileChooser;
import com.nomagic.ui.ScaledFileChooserFactory;
import com.nomagic.ui.SimpleBaseDialog;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Textbox window dialog that allows the user to copy to clipboard or save to file the contents
 * Right now it is pretty hard coded to a use case but could be made generic later with actions
 * <p>
 * <p>
 * These are the resources I used to build this complicated class
 *
 * @see com.nomagic.magicdraw.internalerror.a.b#s()
 * @see com.nomagic.magicdraw.internalerror.a.c#c(Window)
 */
@SuppressWarnings("deprecation")
public class BasicFileTextboxWindowDialog extends SimpleBaseDialog {

    public static final String COPY_TO_CLIPBOARD_TXT = "Copy to clipboard";

    public static final int COPY_TO_CLIPBOARD = 1015;
    public static final int EXPORT_TO_FILE = 1002;
    public static final int CLOSE = 7;
    public static final Hashtable<String, Integer> BUTTON_ACTION_MAP = new Hashtable<>() {{
        put(APPLY, COPY_TO_CLIPBOARD);
        put("EXPORT_TO_FILE...", EXPORT_TO_FILE);
        put("CLOSE", CLOSE);
    }};
    public static final List<String> BUTTON_NAMES = Collections.list(BUTTON_ACTION_MAP.keys());
    public String contents;
    public String header;

    public BasicFileTextboxWindowDialog(String header, String contents, JPanel panel) {
        super(MDDialogParentProvider.getProvider().getDialogOwner(), header, true, BUTTON_NAMES, BUTTON_ACTION_MAP,
                (String) null, 0, panel);
        this.contents = contents;
        this.header = header;

        // button names have to be provided from cameo resources.properties file and there is none for copy
        // we used temporary the APPLY resource and swap it afterward for "Copy to clipboard"
        var copyButton = this.getButtonByAction(APPLY);
        assert copyButton != null;
        copyButton.setText(COPY_TO_CLIPBOARD_TXT);
        copyButton.setName(COPY_TO_CLIPBOARD_TXT);

        addListeners();
    }

    public static BasicFileTextboxWindowDialog create(String header, String contents) {
        var textPane = new JTextPane();
        textPane.setBackground(Color.white);
        textPane.setEditable(false);
        textPane.setText(contents);
        JScrollPane scrollPanel = new JScrollPane(textPane);
        scrollPanel.setPreferredSize(new Dimension(750, 470));
        var jpanel = new JPanel(new BorderLayout());
        jpanel.add(scrollPanel, "Center");
        return new BasicFileTextboxWindowDialog(header, contents, jpanel);
    }

    @Override
    protected boolean onAction(AWTEvent var1, int action_code) {
        switch (action_code) {
            case COPY_TO_CLIPBOARD:
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(this.contents);
                clipboard.setContents(selection, null);
                return false;
            case EXPORT_TO_FILE:
                // todo: save file location for next time brought up
                // todo: update ctor to take default file name and location

                JFileChooser chooser = ScaledFileChooserFactory.createFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("txt files", "txt"));
                chooser.setDialogTitle("Export to txt File");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setSelectedFile(new File("output.txt"));
                if ((new MDFileChooser(chooser)).showSaveDialog(this) == 0) {
                    try {
                        FileUtils.writeStringToFile(chooser.getSelectedFile(), this.contents);
                    } catch (Exception ex) {
                        System.out.println("Failed to write to file: " + chooser.getSelectedFile().getAbsolutePath());
                    }
                }
                return false;
        }
        return true;
    }
}
