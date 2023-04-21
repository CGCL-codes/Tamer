package tico.imageGallery.actions;

import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JOptionPane;
import tico.configuration.TLanguage;
import tico.editor.TEditor;
import tico.imageGallery.dataBase.TIGDataBase;
import tico.imageGallery.dialogs.TIGModifyImageDialog;

public class TIGManageImageAction extends TIGAbstractAction {

    private TIGDataBase dataBase;

    /**
	 * Constructor for TIGOpenGallery.
	 * 
	 * @param editor The boards' editor
	 */
    public TIGManageImageAction(TEditor editor, TIGDataBase dataBase) {
        super(editor);
        this.dataBase = dataBase;
    }

    public void actionPerformed(ActionEvent e) {
        TIGDataBase.conectDB();
        Vector<Vector<String>> data = TIGDataBase.imageSearchByName("*");
        if (data.size() > 0) new TIGModifyImageDialog(getEditor(), dataBase); else JOptionPane.showConfirmDialog(null, TLanguage.getString("TIGManageImageAction.EMPTY"), TLanguage.getString("TIGManageImageAction.ERROR"), JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
    }
}
