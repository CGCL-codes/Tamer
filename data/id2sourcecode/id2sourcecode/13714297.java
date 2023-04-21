    public SnapshotAttributeDeltaAbsValue(SnapshotAttributeWriteAbsValue writeAbsValue, SnapshotAttributeReadAbsValue readAbsValue, boolean manageAllTypes) {
        super(writeAbsValue.getDataFormat(), writeAbsValue.getDataType(), null);
        Object deltaValue = getDeltaAbsValue(writeAbsValue, readAbsValue, manageAllTypes);
        if (readAbsValue == null || writeAbsValue == null) {
            this.setNotApplicable(true);
        } else if (readAbsValue.isNotApplicable() || writeAbsValue.isNotApplicable()) {
            this.setNotApplicable(true);
        } else if (deltaValue == null) {
            this.setNotApplicable(true);
        }
        if ((deltaValue instanceof String) || (deltaValue instanceof String[])) {
            this.setDataType(TangoConst.Tango_DEV_STRING);
        }
        this.setValue(deltaValue);
    }
