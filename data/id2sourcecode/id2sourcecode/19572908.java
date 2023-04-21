    public static SnapshotAttribute createDummyAttribute(final SnapshotAttribute attribute) {
        SnapshotAttribute ret = new SnapshotAttribute();
        ret.setAttribute_complete_name(attribute.getAttribute_complete_name());
        SnapshotAttributeReadValue readValue = new SnapshotAttributeReadValue(0, 0, null);
        readValue.setNotApplicable(true);
        ret.setReadValue(readValue);
        SnapshotAttributeWriteValue writeValue = new SnapshotAttributeWriteValue(0, 0, null);
        writeValue.setNotApplicable(true);
        ret.setWriteValue(writeValue);
        SnapshotAttributeDeltaValue deltaValue = new SnapshotAttributeDeltaValue(writeValue, readValue);
        deltaValue.setNotApplicable(true);
        ret.setDeltaValue(deltaValue);
        ret.setData_type(attribute.getData_type());
        ret.setData_format(attribute.getData_format());
        return ret;
    }
