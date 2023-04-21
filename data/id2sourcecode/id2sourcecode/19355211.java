    private Object getDeltaValue(SnapshotAttributeWriteValue writeValue, SnapshotAttributeReadValue readValue, boolean manageAllTypes) {
        switch(this.dataFormat) {
            case SCALAR_DATA_FORMAT:
                return getScalarDeltaValue(writeValue, readValue, manageAllTypes);
            case SPECTRUM_DATA_FORMAT:
                return getSpectrumDeltaValue(writeValue, readValue, manageAllTypes);
            case IMAGE_DATA_FORMAT:
                return getImageDeltaValue(writeValue, readValue, manageAllTypes);
            default:
                return null;
        }
    }
