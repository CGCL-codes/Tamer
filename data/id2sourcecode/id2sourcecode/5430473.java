    private String getChannelKey(BluetoothDevice remoteDevice, int uuid) {
        return remoteDevice.getAddress() + "_" + Integer.toHexString(uuid);
    }
