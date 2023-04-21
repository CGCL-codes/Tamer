    public Object getValueAt(int row, int column) {
        Remote remote = remoteConfig.getRemote();
        DeviceButton db = getRow(row);
        short[] data = getData(row);
        int typeIndex = getExtendedTypeIndex(row);
        int group = db.getDeviceGroup(data);
        column = getEffectiveColumn(column);
        if (typeIndex == 0xFF && column > 1 && column < 7) {
            return null;
        }
        switch(column) {
            case 0:
                return new Integer(row + 1);
            case 1:
                return db.getName();
            case 2:
                {
                    return remote.getDeviceTypeByIndexAndGroup(typeIndex, group);
                }
            case 3:
                {
                    if (softHT.inUse() && typeIndex == softHT.getDeviceType()) {
                        return null;
                    }
                    short value = db.getSetupCode(data);
                    return value < 0 ? null : new SetupCode(value);
                }
            case 4:
                {
                    return db.getVolumePT();
                }
            case 5:
                {
                    return db.getTransportPT();
                }
            case 6:
                {
                    return db.getChannelPT();
                }
            case 7:
                {
                    String[] notes = remoteConfig.getDeviceButtonNotes();
                    String note = null;
                    if (notes != null) {
                        note = notes[row];
                    }
                    if (note == null) {
                        DeviceUpgrade deviceUpgrade = remoteConfig.getAssignedDeviceUpgrade(db);
                        if (deviceUpgrade != null) {
                            note = deviceUpgrade.getDescription();
                        }
                    }
                    if (note == null) {
                        return "";
                    } else {
                        return note;
                    }
                }
            case 8:
                {
                    DeviceLabels labels = remote.getDeviceLabels();
                    return labels.getText(data, row);
                }
            case 9:
                {
                    SoftDevices softDevices = remote.getSoftDevices();
                    int seq = softDevices.getSequencePosition(row, getRowCount(), data);
                    if (seq == -1) {
                        return null;
                    } else {
                        return seq + 1;
                    }
                }
            case 10:
                {
                    return db.getHighlight();
                }
            default:
                return null;
        }
    }
