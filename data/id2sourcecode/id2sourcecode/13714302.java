    private Object getSpectrumDeltaAbsValue(SnapshotAttributeWriteAbsValue writeAbsValue, SnapshotAttributeReadAbsValue readAbsValue, boolean manageAllTypes) {
        if (writeAbsValue == null || readAbsValue == null) {
            return null;
        }
        Object readAbsValueTab = readAbsValue.getSpectrumValue();
        Object writeAbsValueTab = writeAbsValue.getSpectrumValue();
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
                if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                    readDouble = (Double[]) readAbsValueTab;
                    readLength = readDouble.length;
                }
                if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                    writeDouble = (Double[]) writeAbsValueTab;
                    writeLength = writeDouble.length;
                }
                break;
            case TangoConst.Tango_DEV_FLOAT:
                if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                    readFloat = (Float[]) readAbsValueTab;
                    readLength = readFloat.length;
                }
                if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                    writeFloat = (Float[]) writeAbsValueTab;
                    writeLength = writeFloat.length;
                }
                break;
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                    readShort = (Short[]) readAbsValueTab;
                    readLength = readShort.length;
                }
                if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                    writeShort = (Short[]) writeAbsValueTab;
                    writeLength = writeShort.length;
                }
                break;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                    readChar = (Byte[]) readAbsValueTab;
                    readLength = readChar.length;
                }
                if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                    writeChar = (Byte[]) writeAbsValueTab;
                    writeLength = writeChar.length;
                }
                break;
            case TangoConst.Tango_DEV_STATE:
                if (!manageAllTypes) {
                    break;
                }
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                    readLong = (Integer[]) readAbsValueTab;
                    readLength = readLong.length;
                }
                if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                    writeLong = (Integer[]) writeAbsValueTab;
                    writeLength = writeLong.length;
                }
                break;
            case TangoConst.Tango_DEV_BOOLEAN:
                if (manageAllTypes) {
                    if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                        readBoolean = (Boolean[]) readAbsValueTab;
                        readLength = readBoolean.length;
                    }
                    if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                        writeBoolean = (Boolean[]) writeAbsValueTab;
                        writeLength = writeBoolean.length;
                    }
                }
                break;
            case TangoConst.Tango_DEV_STRING:
                if (manageAllTypes) {
                    if (readAbsValueTab != null && !"Nan".equals(readAbsValueTab)) {
                        readString = (String[]) readAbsValueTab;
                        readLength = readString.length;
                    }
                    if (writeAbsValueTab != null && !"Nan".equals(writeAbsValueTab)) {
                        writeString = (String[]) writeAbsValueTab;
                        writeLength = writeString.length;
                    }
                }
                break;
            default:
        }
        if (readAbsValueTab == null || readLength == 0) {
            return null;
        }
        if (writeAbsValueTab == null || writeLength == 0) {
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
                    diffDouble[i] = new Double(Math.abs(readDouble[i].doubleValue() - writeDouble[i].doubleValue()));
                }
                ret = diffDouble;
                break;
            case TangoConst.Tango_DEV_FLOAT:
                diffFloat = new Float[readLength];
                for (int i = 0; i < diffFloat.length; i++) {
                    diffFloat[i] = new Float(Math.abs(readFloat[i].floatValue() - writeFloat[i].floatValue()));
                }
                ret = diffFloat;
                break;
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                diffShort = new Short[readLength];
                for (int i = 0; i < diffShort.length; i++) {
                    diffShort[i] = new Short((short) Math.abs(readShort[i].shortValue() - writeShort[i].shortValue()));
                }
                ret = diffShort;
                break;
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                diffChar = new Byte[readLength];
                for (int i = 0; i < diffChar.length; i++) {
                    diffChar[i] = new Byte((byte) Math.abs(readChar[i].byteValue() - writeChar[i].byteValue()));
                }
                ret = diffChar;
                break;
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                diffLong = new Integer[readLength];
                for (int i = 0; i < diffLong.length; i++) {
                    diffLong[i] = new Integer(Math.abs(readLong[i].intValue() - writeLong[i].intValue()));
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
