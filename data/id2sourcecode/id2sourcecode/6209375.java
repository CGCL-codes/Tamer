        private void processConditionMetEvent(NativeEvent nativeEvt) {
            Sensor sensor = SensorRegistry.getSensor(nativeEvt.intParam2);
            if (sensor != null) {
                ChannelImpl channel = (ChannelImpl) sensor.getChannelInfos()[nativeEvt.intParam3];
                if (channel != null) {
                    ConditionListenerPair pair;
                    while ((pair = channel.getCondPair()) != null) {
                        try {
                            pair.getListener().conditionMet(sensor, pair.getData(), pair.getCondition());
                        } catch (Exception exc) {
                        }
                    }
                }
            }
        }
