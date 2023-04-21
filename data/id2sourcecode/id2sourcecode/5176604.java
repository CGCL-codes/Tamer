    private void testSensorCreation() throws BadConfigurationException {
        assertEquals("Sensors count is equal", SENSOR_COUNT, SensorRegistry.getSensorCount());
        SensorInfo[] infos = SensorRegistry.getAllSensors();
        for (int i = 0; i < SENSOR_COUNT; i++) {
            assertEquals("Quantity equals", QUANTITY[i], infos[i].getQuantity());
            assertEquals("Max buffer size equals", MAX_BUF_SIZE[i], infos[i].getMaxBufferSize());
            assertEquals("Context type equals", CONTEXT[i], infos[i].getContextType());
            assertEquals("Url equals", URL[i], infos[i].getUrl());
            ChannelInfo[] channels = infos[i].getChannelInfos();
            assertEquals("Channels count equals", CHANNEL_COUNT[i], channels.length);
        }
    }
