package net.adrianromero.editor;

import java.awt.Toolkit;
import javax.swing.Icon;
import net.adrianromero.basic.BasicException;
import net.adrianromero.format.Formats;

public abstract class JEditorNumber extends JEditorAbstract {

    private static final int NUMBER_ZERONULL = 0;

    private static final int NUMBER_INT = 1;

    private static final int NUMBER_DEC = 2;

    private int m_iNumberStatus;

    private String m_sNumber;

    private boolean m_bNegative;

    private Formats m_fmt;

    /** Creates a new instance of JEditorNumber */
    public JEditorNumber() {
        m_fmt = getFormat();
        reset();
    }

    protected abstract Formats getFormat();

    public void reset() {
        String sOldText = getText();
        m_sNumber = "";
        m_bNegative = false;
        m_iNumberStatus = NUMBER_ZERONULL;
        reprintText();
        firePropertyChange("Text", sOldText, getText());
    }

    public void setValue(double dvalue) {
        String sOldText = getText();
        if (dvalue >= 0.0) {
            m_sNumber = formatDouble(dvalue);
            m_bNegative = false;
            m_iNumberStatus = NUMBER_ZERONULL;
        } else {
            m_sNumber = formatDouble(-dvalue);
            m_bNegative = true;
            m_iNumberStatus = NUMBER_ZERONULL;
        }
        reprintText();
        firePropertyChange("Text", sOldText, getText());
    }

    public double getValue() throws BasicException {
        try {
            return Double.parseDouble(getText());
        } catch (NumberFormatException e) {
            throw new BasicException(e);
        }
    }

    public void setValueInteger(int ivalue) {
        String sOldText = getText();
        if (ivalue >= 0) {
            m_sNumber = Integer.toString(ivalue);
            m_bNegative = false;
            m_iNumberStatus = NUMBER_ZERONULL;
        } else {
            m_sNumber = Integer.toString(-ivalue);
            m_bNegative = true;
            m_iNumberStatus = NUMBER_ZERONULL;
        }
        reprintText();
        firePropertyChange("Text", sOldText, getText());
    }

    public int getValueInteger() throws BasicException {
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            throw new BasicException(e);
        }
    }

    private String formatDouble(double dvalue) {
        String sNumber = Double.toString(dvalue);
        if (sNumber.endsWith(".0")) {
            sNumber = sNumber.substring(0, sNumber.length() - 2);
        }
        return sNumber;
    }

    protected String getEditMode() {
        return "-1.23";
    }

    public String getText() {
        return (m_bNegative ? "-" : "") + m_sNumber;
    }

    protected int getAlignment() {
        return javax.swing.SwingConstants.RIGHT;
    }

    protected String getTextEdit() {
        return getText();
    }

    protected String getTextFormat() throws BasicException {
        return m_fmt.formatValue(new Double(getValue()));
    }

    protected void typeCharInternal(char cTrans) {
        transChar(cTrans);
    }

    protected void transCharInternal(char cTrans) {
        String sOldText = getText();
        if (cTrans == '') {
            reset();
        } else if (cTrans == '-') {
            m_bNegative = !m_bNegative;
        } else if ((cTrans == '0') && (m_iNumberStatus == NUMBER_ZERONULL)) {
            m_sNumber = "0";
        } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9') && (m_iNumberStatus == NUMBER_ZERONULL)) {
            m_iNumberStatus = NUMBER_INT;
            m_sNumber = Character.toString(cTrans);
        } else if (cTrans == '.' && m_iNumberStatus == NUMBER_ZERONULL) {
            m_iNumberStatus = NUMBER_DEC;
            m_sNumber = "0.";
        } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9') && (m_iNumberStatus == NUMBER_INT)) {
            m_sNumber += cTrans;
        } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INT) {
            m_iNumberStatus = NUMBER_DEC;
            m_sNumber += '.';
        } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9') && (m_iNumberStatus == NUMBER_DEC)) {
            m_sNumber += cTrans;
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        firePropertyChange("Text", sOldText, getText());
    }
}
