        private void processChannelMessageQueue(NativeEvent nativeEvt) {
            Sensor sensor = SensorRegistry.getSensor(nativeEvt.intParam2);
            if (sensor != null) {
                ChannelImpl channel = (ChannelImpl) sensor.getChannelInfos()[nativeEvt.intParam3];
                if (channel != null) {
                    channel.processMessage();
                }
            }
        }
