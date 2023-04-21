        public void send(Channel channel, Buffer data) throws Exception {
            open(channel);
            channel.socketChannel = SocketChannel.open();
            Context.getInstance().getChannelManager().onConnectRequired(channel);
            channel.socketChannel.connect(new InetSocketAddress(channel.gateProps.getIP(), channel.gateProps.getPort()));
            channel.writeState.write(channel, data);
        }
