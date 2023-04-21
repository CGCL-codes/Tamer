    public void receiveRPY(Message message) throws AbortChannelException {
        byte[] data = read(message);
        try {
            QueryInfo info = (QueryInfo) message.getChannel().getAppData();
            handler.setMetaData(info);
            deserializer.deserialize(data, handler);
            Request request = (Request) handler.getResult();
            if (message.getMessageType() == Message.MESSAGE_TYPE_MSG) {
                request.setMessage((MessageMSG) message);
            }
            filter.process(request);
        } catch (DeserializeException de) {
            LOG.error("could not deserialize [" + de.getMessage() + "]");
        }
    }
