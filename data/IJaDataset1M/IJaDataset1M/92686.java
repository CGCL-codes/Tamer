package mobi.hilltop.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class MiniTableImpl implements MiniTable {

    private Hashtable columns = new Hashtable();

    private Vector rows = new Vector();

    private String name = "";

    private int length = -1;

    public MiniTableImpl() {
    }

    public String getCellValue(int rowNo, String colName) {
        return getCellValue(rowNo, colName, true);
    }

    public String getCellValue(int rowNo, String colName, boolean format) {
        Object colObj = columns.get(colName.toLowerCase());
        if (null == colObj) {
            if (format) {
                return "";
            }
            return null;
        }
        return getCellValue(rowNo, ((Integer) colObj).intValue(), format);
    }

    public String getCellValue(int rowNo, int colNo) {
        return getCellValue(rowNo, colNo, true);
    }

    public String getCellValue(int rowNo, int colNo, boolean format) {
        Vector row = (Vector) rows.elementAt(rowNo);
        Object obj = row.elementAt(colNo);
        if (null == obj) {
            if (format) {
                return "";
            }
            return null;
        }
        return obj.toString();
    }

    public Object getCellObjectValue(int rowNo, String colName) {
        Object colObj = columns.get(colName.toLowerCase());
        if (null == colObj) {
            return null;
        }
        return getCellObjectValue(rowNo, ((Integer) colObj).intValue());
    }

    public Object getCellObjectValue(int rowNo, int colNo) {
        Vector row = (Vector) rows.elementAt(rowNo);
        return row.elementAt(colNo);
    }

    public void setCellValue(int row, String colName, Object obj) {
        Object colObj = columns.get(colName.toLowerCase());
        if (colObj == null) colObj = columns.get(colName.toUpperCase());
        ((Vector) this.rows.elementAt(row)).setElementAt(obj, ((Integer) colObj).intValue());
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columns.size();
    }

    public String getColumnName(int index) {
        Enumeration itr = columns.keys();
        while (itr.hasMoreElements()) {
            String name = String.valueOf(itr.nextElement());
            int location = Integer.parseInt(String.valueOf(columns.get(name)));
            if (location == index) {
                return name;
            }
        }
        return null;
    }

    public Hashtable getColumns() {
        return columns;
    }

    public void setColumns(Hashtable columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 增加一行
     * 
     * @param row
     */
    public void addRow(Vector row) {
        rows.addElement(row);
    }

    public void addColumns(String name) {
        columns.put(name.toLowerCase(), new Integer(columns.size()));
    }

    public void createRow() {
        Vector row = new Vector();
        for (int i = 0; i < this.getColumnCount(); i++) {
            row.addElement(null);
        }
        this.rows.addElement(row);
    }

    public void setCellValue(int row, int col, Object obj) {
        ((Vector) this.rows.elementAt(row)).setElementAt(obj, col);
    }

    public byte[] serialize() {
        byte[] ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(name);
            dos.writeInt(columns.size());
            dos.writeInt(rows.size());
            Enumeration itr = columns.keys();
            while (itr.hasMoreElements()) {
                String colKey = String.valueOf(itr.nextElement());
                String index = String.valueOf(columns.get(colKey));
                dos.writeUTF(colKey);
                dos.writeUTF(index);
            }
            for (int j = 0; j < rows.size(); j++) {
                for (int i = 0; i < columns.size(); i++) {
                    dos.writeUTF(getCellValue(j, i));
                }
            }
            ret = baos.toByteArray();
            length = ret.length;
            dos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void deserialize(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);
        try {
            name = dis.readUTF();
            int columnsSize = dis.readInt();
            int rowsSize = dis.readInt();
            Hashtable cols = new Hashtable();
            for (int i = 0; i < columnsSize; i++) {
                String colKey = dis.readUTF();
                String index = dis.readUTF();
                cols.put(colKey, Integer.valueOf(index));
            }
            this.setColumns(cols);
            for (int i = 0; i < rowsSize; i++) {
                createRow();
                for (int j = 0; j < columnsSize; j++) {
                    setCellValue(i, j, dis.readUTF());
                }
            }
            dis.close();
            bais.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLength() {
        return length;
    }
}
