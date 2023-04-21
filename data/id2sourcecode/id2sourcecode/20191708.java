    protected void closeNewChannels() throws IOReactorException {
        ChannelEntry entry;
        while ((entry = this.newChannels.poll()) != null) {
            SessionRequestImpl sessionRequest = entry.getSessionRequest();
            if (sessionRequest != null) {
                sessionRequest.cancel();
            }
            SocketChannel channel = entry.getChannel();
            try {
                channel.close();
            } catch (IOException ignore) {
            }
        }
    }
