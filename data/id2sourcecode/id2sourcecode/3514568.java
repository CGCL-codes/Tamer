    private EventConsumer isChannelAlreadyConnected(DeviceProxy deviceProxy) {
        try {
            String adminName = deviceProxy.adm_name();
            EventChannelStruct eventChannelStruct = EventConsumer.getChannelMap().get(adminName);
            if (eventChannelStruct == null) {
                return null;
            } else {
                return eventChannelStruct.consumer;
            }
        } catch (DevFailed e) {
            return null;
        }
    }
