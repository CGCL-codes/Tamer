package de.shandschuh.jaolt.core;

import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import de.shandschuh.jaolt.gui.Lister;

public class Warning {

    private static Vector<Integer> HASHES = new Vector<Integer>();

    public static synchronized void init(Vector<Integer> USERHASHES) {
        HASHES = USERHASHES;
    }

    public static Vector<Integer> currentHashes() {
        return HASHES;
    }

    public static synchronized void show(String text, int hash) {
        if (!HASHES.contains(hash)) {
            JCheckBox doNotShowAgainJCheckBox = new JCheckBox(Language.translateStatic("CHECKBOX_DONTSHOWTHISAGAIN_TEXT"));
            Object[] message = new Object[3];
            message[0] = text;
            message[1] = new JPanel();
            message[2] = doNotShowAgainJCheckBox;
            JOptionPane.showConfirmDialog(Lister.getCurrentInstance(), message, Language.translateStatic("DIALOG_WARNING_TITLE"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            if (doNotShowAgainJCheckBox.isSelected()) {
                HASHES.add(hash);
            }
        }
    }
}
