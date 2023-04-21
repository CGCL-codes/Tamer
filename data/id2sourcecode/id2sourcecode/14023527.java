    protected void validateServiceRecord(ServiceRecord srvRecord) {
        if (this.rfcommChannel != serviceRecord.getChannel(BluetoothConsts.RFCOMM_PROTOCOL_UUID)) {
            throw new IllegalArgumentException("Must not change the RFCOMM server channel number");
        }
        super.validateServiceRecord(srvRecord);
    }
