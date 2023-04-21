    public static SnapshotAttributeDeltaValue getInstance(SnapshotAttributeWriteValue writeValue, SnapshotAttributeReadValue readValue) {
        switch(writeValue.getDataType()) {
            case TangoConst.Tango_DEV_FLOAT:
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_DOUBLE:
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_UCHAR:
            case TangoConst.Tango_DEV_CHAR:
                return new SnapshotAttributeDeltaValue(writeValue, readValue);
            default:
                return new SnapshotAttributeDeltaValue(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
        }
    }
