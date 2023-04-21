    @SuppressWarnings("unchecked")
    private void startPlugin(final Plugin plugin) throws ConnectingException {
        for (final Channel channel : (Vector<Channel>) plugin.getChannels()) {
            if ((channel instanceof PluginChannel) && (channel.getTransport() instanceof JvmTransport)) {
                final JvmTransport transport = (JvmTransport) channel.getTransport();
                final ChannelServer channelServer = getChannelServer();
                transport.setChannelServer(channelServer);
            }
        }
        plugin.connect();
    }
