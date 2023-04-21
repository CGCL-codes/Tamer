    private void send(final User user, final UserFeedBean feedBean) {
        String serialized = null;
        try {
            serialized = RPC.encodeResponseForSuccess(this.serviceMethod, feedBean, this.serializationPolicy);
        } catch (SerializationException exception) {
            exception.printStackTrace();
        }
        try {
            ChannelServiceFactory.getChannelService().sendMessage(new ChannelMessage(user.getKey(), serialized));
        } catch (IllegalArgumentException exception) {
            System.out.println("Serialized length:" + serialized.length());
            System.out.println(serialized);
        }
    }
