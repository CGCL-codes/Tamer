    public static SnapshotAttributeDeltaAbsValue getInstance(SnapshotAttributeWriteAbsValue writeAbsValue, SnapshotAttributeReadAbsValue readAbsValue) {
        switch(writeAbsValue.getDataType()) {
            case TangoConst.Tango_DEV_FLOAT:
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_DOUBLE:
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                return new SnapshotAttributeDeltaAbsValue(writeAbsValue, readAbsValue);
            default:
                return new SnapshotAttributeDeltaAbsValue(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
        }
    }
