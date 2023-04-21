    public BluetoothRFCommConnectionNotifier(BluetoothStack bluetoothStack, BluetoothConnectionNotifierParams params) throws IOException {
        super(bluetoothStack, params);
        this.handle = bluetoothStack.rfServerOpen(params, serviceRecord);
        this.rfcommChannel = serviceRecord.getChannel(BluetoothConsts.RFCOMM_PROTOCOL_UUID);
        this.serviceRecord.attributeUpdated = false;
        this.securityOpt = Utils.securityOpt(params.authenticate, params.encrypt);
        this.connectionCreated();
    }
