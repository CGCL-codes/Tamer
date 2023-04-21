    private boolean isDeviceUosCompliant(RemoteDevice device, String deviceName) {
        int retries = 0;
        while (retries < MAX_CONNECT_RETRIES) {
            try {
                logger.debug("[BluetoothRadar] Checking if " + deviceName + " is uOS compliant. " + (retries + 1) + " of " + MAX_CONNECT_RETRIES + " chances.");
                ClientConnection cc = ((BluetoothChannelManager) connectionManager.getChannelManager()).openActiveConnection(device.getBluetoothAddress());
                cc.closeConnection();
                logger.debug("[BluetoothRadar] " + deviceName + " is uOS compliant.");
                return true;
            } catch (Exception e) {
                logger.info("Failed to connect to open a control channel with device " + deviceName, e);
                retries++;
                if (retries < MAX_CONNECT_RETRIES) {
                    try {
                        Thread.sleep(WAIT_TIME_BETWEEN_RETRIES);
                    } catch (InterruptedException e1) {
                        logger.error("Failed to connect to open a control channel with device " + deviceName, e1);
                    }
                }
            }
        }
        return false;
    }
