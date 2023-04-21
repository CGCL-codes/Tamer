    public Object getValueAt(int rowIndex, int columnIndex) {
        final MMRecord record;
        synchronized (_mpsRecords) {
            record = (MMRecord) _mpsRecords.get(rowIndex);
        }
        switch(columnIndex) {
            case 0:
                return record.getPV();
            case 1:
                return record.getSubSystem();
            case 2:
                return record.getDevice();
            case 3:
                return record.getIOC();
            case 4:
                return record.getChannelNo();
            case 5:
                return record.getTestedValue();
            case 6:
                return record.getTestRdyValue();
            default:
                return "";
        }
    }
