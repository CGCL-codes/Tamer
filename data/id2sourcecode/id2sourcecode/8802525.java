    public BluetoothL2CAPConnectionNotifier(BluetoothStack bluetoothStack, BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU) throws IOException {
        super(bluetoothStack, params);
        this.handle = bluetoothStack.l2ServerOpen(params, receiveMTU, transmitMTU, serviceRecord);
        this.psm = serviceRecord.getChannel(BluetoothConsts.L2CAP_PROTOCOL_UUID);
        this.serviceRecord.attributeUpdated = false;
        this.securityOpt = Utils.securityOpt(params.authenticate, params.encrypt);
        this.connectionCreated();
    }
