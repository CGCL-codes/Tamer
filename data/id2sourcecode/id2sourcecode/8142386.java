    private ChannelExec getChannel() throws JSchException {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(this.command);
        if (environmentProperties != null) {
            for (String key : environmentProperties.keySet()) {
                logger.debug("setting environment property: " + key + "=" + environmentProperties.get(key));
                channel.setEnv(key, (String) environmentProperties.get(key));
            }
        }
        channel.connect();
        return channel;
    }
