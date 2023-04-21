    public void decode(Packet packet) {
        channelId = packet.getHeader().getChannelId();
        streamId = packet.getHeader().getStreamId();
        AmfObject object = new AmfObject();
        object.decode(packet.getData(), false);
        List<AmfProperty> properties = object.getProperties();
        methodName = (String) properties.get(0).getValue();
        double temp = (Double) properties.get(1).getValue();
        sequenceId = (int) temp;
        if (properties.size() > 2) {
            int argsLength = properties.size() - 2;
            args = new Object[argsLength];
            for (int i = 0; i < argsLength; i++) {
                args[i] = properties.get(i + 2).getValue();
            }
        }
        logger.info("decoded invoke: " + toString());
    }
