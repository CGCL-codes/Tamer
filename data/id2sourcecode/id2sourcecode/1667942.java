    protected void publish(List<IValueObject> vos, String channelName) {
        if (log.isDebugEnabled()) {
            log.debug("Publishing Message. Channel = " + channelName + " Value Object = " + vos.get(0).toString());
        }
        if (!channelName.contains("//")) {
            if (CometClientServlet.getBayeux() != null) {
                ServerChannel channel = CometClientServlet.getBayeux().getChannel(channelName);
                if (channel == null && hasWildcardListener(channelName)) {
                    CometClientServlet.getBayeux().createIfAbsent(channelName, new ConfigurableServerChannel.Initializer() {

                        @Override
                        public void configureChannel(ConfigurableServerChannel channel) {
                        }
                    });
                    channel = CometClientServlet.getBayeux().getChannel(channelName);
                }
                if (channel != null) {
                    Map<String, Object> msg = JSONConverter.convertToJSON(vos.get(0));
                    for (int i = 1; i < vos.size(); i++) {
                        msg.put("msg" + (i + 1), JSONConverter.convertToJSON(vos.get(i)));
                    }
                    channel.publish(CometClientServlet.getClient(), msg, channelName);
                }
            }
        }
    }
