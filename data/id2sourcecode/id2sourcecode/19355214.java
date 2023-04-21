    private Object getSpectrumDeltaValue(SnapshotAttributeWriteValue writeValue, SnapshotAttributeReadValue readValue, boolean manageAllTypes) {
        if (writeValue == null || readValue == null) {
            return null;
        }
        Object readValueTab = readValue.getSpectrumValue();
        Object writeValueTab = writeValue.getSpectrumValue();
        int readLength = 0;
        int writeLength = 0;
        Byte[] readChar = null, writeChar = null, diffChar = null;
        Integer[] readLong = null, writeLong = null, diffLong = null;
        Short[] readShort = null, writeShort = null, diffShort = null;
        Float[] readFloat = null, writeFloat = null, diffFloat = null;
        Double[] readDouble = null, writeDouble = null, diffDouble = null;
        Boolean[] readBoolean = null, writeBoolean = null;
        String[] readString = null, writeString = null, diffString = null;
        switch(dataType) {
            case TangoConst.Tango_DEV_DOUBLE:
                if (readValueTab != null && !"Nan".equals(readValueTab)) {
                    readDouble = (Double[]) readValueTab;
                    readLength = readDouble.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                    writeDouble = (Double[]) writeValueTab;
                    writeLength = writeDouble.length;
                }
                break;
            case TangoConst.Tango_DEV_FLOAT:
                if (readValueTab != null && !"Nan".equals(readValueTab)) {
                    readFloat = (Float[]) readValueTab;
                    readLength = readFloat.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                    writeFloat = (Float[]) writeValueTab;
                    writeLength = writeFloat.length;
                }
                break;
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                if (readValueTab != null && !"Nan".equals(readValueTab)) {
                    readShort = (Short[]) readValueTab;
                    readLength = readShort.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                    writeShort = (Short[]) writeValueTab;
                    writeLength = writeShort.length;
                }
                break;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                if (readValueTab != null && !"Nan".equals(readValueTab)) {
                    readChar = (Byte[]) readValueTab;
                    readLength = readChar.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                    writeChar = (Byte[]) writeValueTab;
                    writeLength = writeChar.length;
                }
                break;
            case TangoConst.Tango_DEV_STATE:
                if (!manageAllTypes) {
                    break;
                }
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                if (readValueTab != null && !"Nan".equals(readValueTab)) {
                    readLong = (Integer[]) readValueTab;
                    readLength = readLong.length;
                }
                if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                    writeLong = (Integer[]) writeValueTab;
                    writeLength = writeLong.length;
                }
                break;
            case TangoConst.Tango_DEV_BOOLEAN:
                if (manageAllTypes) {
                    if (readValueTab != null && !"Nan".equals(readValueTab)) {
                        readBoolean = (Boolean[]) readValueTab;
                        readLength = readBoolean.length;
                    }
                    if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                        writeBoolean = (Boolean[]) writeValueTab;
                        writeLength = writeBoolean.length;
                    }
                }
                break;
            case TangoConst.Tango_DEV_STRING:
                if (manageAllTypes) {
                    if (readValueTab != null && !"Nan".equals(readValueTab)) {
                        readString = (String[]) readValueTab;
                        readLength = readString.length;
                    }
                    if (writeValueTab != null && !"Nan".equals(writeValueTab)) {
                        writeString = (String[]) writeValueTab;
                        writeLength = writeString.length;
                    }
                }
                break;
            default:
        }
        if (readValueTab == null || readLength == 0) {
            return null;
        }
        if (writeValueTab == null || writeLength == 0) {
            return null;
        }
        if (readLength != writeLength) {
            return null;
        }
        Object[] ret = null;
        switch(dataType) {
            case TangoConst.Tango_DEV_DOUBLE:
                diffDouble = new Double[readLength];
                for (int i = 0; i < diffDouble.length; i++) {
                    diffDouble[i] = new Double(readDouble[i].doubleValue() - writeDouble[i].doubleValue());
                }
                ret = diffDouble;
                break;
            case TangoConst.Tango_DEV_FLOAT:
                diffFloat = new Float[readLength];
                for (int i = 0; i < diffFloat.length; i++) {
                    diffFloat[i] = new Float(readFloat[i].floatValue() - writeFloat[i].floatValue());
                }
                ret = diffFloat;
                break;
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                diffShort = new Short[readLength];
                for (int i = 0; i < diffShort.length; i++) {
                    diffShort[i] = new Short((short) (readShort[i].shortValue() - writeShort[i].shortValue()));
                }
                ret = diffShort;
                break;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                diffChar = new Byte[readLength];
                for (int i = 0; i < diffChar.length; i++) {
                    diffChar[i] = new Byte((byte) (readChar[i].byteValue() - writeChar[i].byteValue()));
                }
                ret = diffChar;
                break;
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                diffLong = new Integer[readLength];
                for (int i = 0; i < diffLong.length; i++) {
                    diffLong[i] = new Integer(readLong[i].intValue() - writeLong[i].intValue());
                }
                ret = diffLong;
                break;
            case TangoConst.Tango_DEV_STATE:
                if (manageAllTypes) {
                    diffString = new String[readLength];
                    for (int i = 0; i < diffString.length; i++) {
                        if (readLong[i] == null && writeLong[i] == null) {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else if (readLong[i] != null && readLong[i].equals(writeLong[i])) {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                        }
                    }
                    ret = diffString;
                }
                break;
            case TangoConst.Tango_DEV_BOOLEAN:
                if (manageAllTypes) {
                    diffString = new String[readLength];
                    for (int i = 0; i < diffString.length; i++) {
                        if (readBoolean[i] == null && writeBoolean[i] == null) {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else if (readBoolean[i] != null && readBoolean[i].equals(writeBoolean[i])) {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                        }
                    }
                    ret = diffString;
                }
                break;
            case TangoConst.Tango_DEV_STRING:
                if (manageAllTypes) {
                    diffString = new String[readLength];
                    for (int i = 0; i < diffString.length; i++) {
                        if (readString[i] == null && writeString[i] == null) {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else if (readString[i] != null && readString[i].equals(writeString[i])) {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else {
                            diffString[i] = Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                        }
                    }
                    ret = diffString;
                }
                break;
            default:
        }
        return ret;
    }
