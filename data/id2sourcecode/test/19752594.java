    public void open() throws IOException {
        if (state != STATE_CLOSED) {
            throw new IOException("Sensor is already opened");
        }
        boolean isInitOk = true;
        isInitOk &= sensorDevice.initSensor();
        for (int i = 0; isInitOk && i < channels.length; i++) {
            isInitOk &= channels[i].getChannelDevice().initChannel();
        }
        if (!isInitOk) {
            throw new IOException("Sensor start fails");
        }
        state = STATE_OPENED;
    }
