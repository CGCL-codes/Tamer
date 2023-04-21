    private Object getDeltaAbsValue(SnapshotAttributeWriteAbsValue writeAbsValue, SnapshotAttributeReadAbsValue readAbsValue, boolean manageAllTypes) {
        switch(this.dataFormat) {
            case SCALAR_DATA_FORMAT:
                return getScalarDeltaAbsValue(writeAbsValue, readAbsValue, manageAllTypes);
            case SPECTRUM_DATA_FORMAT:
                return getSpectrumDeltaAbsValue(writeAbsValue, readAbsValue, manageAllTypes);
            case IMAGE_DATA_FORMAT:
                return getImageDeltaAbsValue(writeAbsValue, readAbsValue, manageAllTypes);
            default:
                return null;
        }
    }
