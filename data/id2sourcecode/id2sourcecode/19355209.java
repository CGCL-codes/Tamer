    public SnapshotAttributeDeltaValue(SnapshotAttributeWriteValue writeValue, SnapshotAttributeReadValue readValue, boolean manageAllTypes) {
        super(writeValue.getDataFormat(), writeValue.getDataType(), null);
        Object deltaValue = getDeltaValue(writeValue, readValue, manageAllTypes);
        if (readValue == null || writeValue == null) {
            this.setNotApplicable(true);
        } else if (readValue.isNotApplicable() || writeValue.isNotApplicable()) {
            this.setNotApplicable(true);
        } else if (deltaValue == null) {
            this.setNotApplicable(true);
        }
        if ((deltaValue instanceof String) || (deltaValue instanceof String[])) {
            this.setDataType(TangoConst.Tango_DEV_STRING);
        }
        this.setValue(deltaValue);
    }
