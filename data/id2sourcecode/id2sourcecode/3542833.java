    public NXTSensorConnection(String url) throws IOException {
        SensorURL sensorURL = SensorURL.parseURL(url);
        NXTSensorInfo[] infos = SensorManager.getSensors(sensorURL);
        if (infos == null || infos.length == 0) {
            infos = SensorManager.findQuantity(sensorURL.getQuantity());
            if (infos == null || infos.length == 0) throw new IOException();
        }
        info = infos[0];
        if (info.getConnectionType() == NXTSensorInfo.CONN_WIRED) {
            int portNumber = -1;
            if (info.getWiredType() == NXTSensorInfo.I2C_SENSOR) {
                SensorURL infoURL = SensorURL.parseURL(info.getUrl());
                portNumber = infoURL.getPortNumber();
            } else {
                portNumber = sensorURL.getPortNumber();
            }
            if (portNumber < 0) throw new IOException();
            port = SensorPort.getInstance(portNumber);
            port.setTypeAndMode(info.getSensorType(), info.getMode());
            if (info.getWiredType() == NXTSensorInfo.I2C_SENSOR) {
                i2cSensor = new I2CSensor(port);
            }
        }
        channelInfos = info.getChannelInfos();
        for (int i = 0; i < channelInfos.length; i++) {
            channels.put(channelInfos[i], new NXTChannel(this, channelInfos[i]));
        }
        state = SensorConnection.STATE_OPENED;
    }
