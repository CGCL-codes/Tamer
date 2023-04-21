    public void broadcastIoSample(IoSample sample) {
        NDC.push("broadcastIoSample");
        long now = System.currentTimeMillis();
        try {
            for (Iterator<String> i = sensorMap.keySet().iterator(); i.hasNext(); ) {
                String compositeAddress = i.next();
                String channel = compositeAddress.substring(18);
                Double value = sample.getChannel(channel);
                logger.debug("channel: " + channel + "=" + value);
                if (value != null) {
                    XBeeSensor sensor = sensorMap.get(compositeAddress);
                    sensor.broadcast(now, value, null);
                }
            }
        } finally {
            NDC.pop();
        }
    }
