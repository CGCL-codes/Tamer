package supersync.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Brandon Drake
 */
public class DateTableCellRenderer extends DefaultTableCellRenderer {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    @Override
    public java.awt.Component getTableCellRendererComponent(JTable l_table, Object l_value, boolean l_isSelected, boolean l_hasFocus, int l_row, int l_column) {
        Date value = (Date) l_value;
        String stringValue = "";
        if (null != value) {
            stringValue = dateFormat.format(value);
        }
        java.awt.Component result = super.getTableCellRendererComponent(l_table, stringValue, l_isSelected, l_hasFocus, l_row, l_column);
        return result;
    }
}
