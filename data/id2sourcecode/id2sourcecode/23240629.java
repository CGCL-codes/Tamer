    public void newValue(final ChannelWrapper wrapper, int value) {
        JTable mpsTable = getMainWindow().getMPSmmTable();
        if (mpsTable == null) return;
        MMRecord MPSmmRecord = null;
        int len = mpsTable.getRowCount();
        String pv = wrapper.getName();
        value = wrapper.getValue();
        int i;
        if (pv.indexOf("sw_jump_status") > 0 && value == 1) {
            numActive++;
            getMainWindow().updateBtn(numMasked, numActive);
            for (i = 0; i < len; i++) {
                if (pv.equals(mpsTable.getValueAt(i, 0))) break;
            }
            if (i == len) {
                MPSmmRecord = new MMRecord(pv, wrapper, _categry);
                if (_categry == 2) {
                    synchronized (_mpsRecords) {
                        _mpsRecords.add(MPSmmRecord);
                    }
                }
            }
        }
        if (pv.indexOf("swmask") > 0 && value == 1) {
            numMasked++;
            getMainWindow().updateBtn(numMasked, numActive);
            len = mpsTable.getRowCount();
            for (i = 0; i < len; i++) {
                if (pv.equals(mpsTable.getValueAt(i, 0))) break;
            }
            if (i == len) {
                MPSmmRecord = new MMRecord(pv, wrapper, _categry);
                if (_categry == 1) {
                    synchronized (_mpsRecords) {
                        _mpsRecords.add(MPSmmRecord);
                    }
                }
            }
        }
        fireTableDataChanged();
        wrapper.getChannel().flushIO();
    }
