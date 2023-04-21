    private ChannelExec getChannel() throws JSchException {
        ChannelExec channel = null;
        try {
            if (this.session == null) {
                connect();
            }
            channel = (ChannelExec) this.session.openChannel("exec");
            channel.setCommand(this.command);
            if (this.environmentProperties != null) {
                for (final String key : this.environmentProperties.keySet()) {
                    LOGGER.debug("setting environment property: " + key + "=" + this.environmentProperties.get(key));
                    channel.setEnv(key, (String) this.environmentProperties.get(key));
                }
            }
            channel.connect();
        } catch (final JSchException e) {
            LOGGER.warn("Failed to receive channel from session. Current command [" + getCommand() + "]. Disconnecting...");
            disconnect();
            this.session = null;
            throw e;
        }
        return channel;
    }
