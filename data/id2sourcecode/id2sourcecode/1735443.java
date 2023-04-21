    public synchronized void newValue(final ChannelWrapper wrapper) {
        if (wrapper == null) return;
        String pvName = wrapper.getName();
        int dot = pvName.lastIndexOf('.');
        if (dot > -1) return;
        JTable mpsTable = getMainWindow().getMPSmmTable();
        if (mpsTable == null) return;
        int len = mpsTable.getRowCount();
        if (len <= 0) return;
        int last = pvName.lastIndexOf(':');
        String SignalName = pvName.substring(0, last);
        String value = "" + wrapper.getFloatValue();
        String ivalue = "" + wrapper.getValue();
        int i;
        for (i = 0; i < len; i++) {
            if (mpsTable.getValueAt(i, 0) == null) return;
            if (SignalName.equals(mpsTable.getValueAt(i, 0))) break;
        }
        if (i >= len) return;
        String hdr = pvName.substring(last + 1);
        int numCol = mpsTable.getColumnCount();
        int j;
        for (j = numCol - 1; j >= 0; j--) {
            if (hdr.equals(HeaderStr[j])) break;
        }
        if (j < 0) return;
        setValueAt(value, i, j);
        if (mmTable != null) {
            mmTable.setValueAt(value, i, j);
        }
        mpsTable.setValueAt(value, i, j);
        mpsTable.validate();
        fireTableDataChanged();
        wrapper.getChannel().flushIO();
    }
