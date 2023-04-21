    public int getChannelData(NXTChannelInfo channelInfo) {
        if (info.getConnectionType() == SensorInfo.CONN_EMBEDDED) {
            return Battery.getVoltageMilliVolt();
        } else if (info.getWiredType() == NXTSensorInfo.I2C_SENSOR) {
            return getI2CChannelData(channelInfo);
        } else {
            return getADChannelData(channelInfo);
        }
    }
