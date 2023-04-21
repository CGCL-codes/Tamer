    public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord) throws IOException {
        EmulatorRFCOMMService s = activeLocalDevice().createRFCOMMService();
        boolean success = false;
        try {
            s.open(params);
            serviceRecord.setHandle(s.getHandle());
            serviceRecord.populateRFCOMMAttributes(s.getHandle(), s.getChannel(), params.uuid, params.name, params.obex);
            s.updateServiceRecord(serviceRecord);
            success = true;
        } finally {
            if (!success) {
                localDevice.removeConnection(s);
            }
        }
        return s.getHandle();
    }
