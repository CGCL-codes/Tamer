package atv;

import java.awt.*;
import javax.swing.text.*;

/**

@author Christian Zmasek

@version 1.01 -- last modified: 06/09/00


*/
class DoubleDocument extends PlainDocument {

    public void insertString(int offset, String s, AttributeSet attributeset) throws BadLocationException {
        if (s.length() != 1 || s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6") || s.equals("7") || s.equals("8") || s.equals("9") || s.equals("0") || s.equals("e") || s.equals("E") || s.equals("-") || s.equals("+") || s.equals(".")) {
            super.insertString(offset, s, attributeset);
        } else {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
    }
}
