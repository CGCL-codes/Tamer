    protected void validateServiceRecord(ServiceRecord srvRecord) {
        if (this.psm != serviceRecord.getChannel(BluetoothConsts.L2CAP_PROTOCOL_UUID)) {
            throw new IllegalArgumentException("Must not change the PSM");
        }
        super.validateServiceRecord(srvRecord);
    }
