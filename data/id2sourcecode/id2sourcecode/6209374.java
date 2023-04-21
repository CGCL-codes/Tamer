        private void processDataCollectEvent(NativeEvent nativeEvt) {
            Sensor sensor = SensorRegistry.getSensor(nativeEvt.intParam2);
            if (sensor != null) {
                ChannelDevice device = sensor.getChannelDevice(nativeEvt.intParam3);
                if (device != null) {
                    ValueListener listener;
                    if ((listener = device.getListener()) != null) {
                        int errorCode = device.measureData();
                        if (errorCode == ValueListener.DATA_READ_OK) {
                            listener.valueReceived(nativeEvt.intParam3, device.getData(), device.getUncertainty(), device.getValidity());
                        } else {
                            listener.dataReadError(nativeEvt.intParam3, errorCode);
                        }
                    }
                }
            }
        }
