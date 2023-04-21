    public void newValue(final ChannelWrapper wrapper, int avalue) {
        String pvName = wrapper.getName();
        JTable mpsTable = getMainWindow().getMPSmmTable();
        if (mpsTable == null) return;
        int len = mpsTable.getRowCount();
        int last = pvName.lastIndexOf(':');
        int j = 0;
        String SignalName = pvName.substring(0, last);
        int i;
        for (i = 0; i < len; i++) {
            if (mpsTable.getValueAt(i, 0) == null) return;
            if (SignalName.equals(mpsTable.getValueAt(i, 0))) break;
        }
        if (i >= len) return;
        String value = "";
        int dot = pvName.lastIndexOf('.');
        String colHdr = "";
        if (dot > -1 && pvName.indexOf("SEVR") == -1) return;
        if (dot > -1) colHdr = pvName.substring(last + 1, dot); else colHdr = pvName.substring(last + 1);
        int numCol = mpsTable.getColumnCount();
        for (j = numCol - 1; j >= 0; j--) {
            if (colHdr.equals(HeaderStr[j])) break;
        }
        if (j < 0) return;
        int alrm = -1;
        String pvStr = "";
        String fval = "";
        if (pvName.indexOf("SEVR") > -1 && wrapper.getValue() <= 0) return;
        if (pvName.indexOf("SEVR") > -1 && wrapper.getValue() > 0) {
            alrm = wrapper.getValue();
            pvStr = SignalName + ":" + colHdr;
            fval = getPVvalue(pvStr, 1);
        } else {
            pvStr = pvName + ".SEVR";
            String sv = getSVvalue(pvName, j);
            try {
                alrm = Integer.parseInt(sv);
            } catch (NumberFormatException e) {
            }
            fval = "" + wrapper.getFloatValue();
        }
        value = "?";
        if (alrm == 1) value = "<html><body><font COLOR=#ffff00>" + fval + "</font></body></html>"; else if (alrm == 2) value = "<html><body><font COLOR=#ff0000>" + fval + "</font></body></html>"; else if (alrm == 3) value = "<html><body><font COLOR=#ffffff>" + fval + "</font></body></html>"; else value = "" + wrapper.getFloatValue();
        setValueAt(value, i, j);
        newValueAt(value, i, j);
        mpsTable.setValueAt(value, i, j);
        mpsTable.validate();
        fireTableCellUpdated(i, j);
        fireTableDataChanged();
        wrapper.getChannel().flushIO();
    }
