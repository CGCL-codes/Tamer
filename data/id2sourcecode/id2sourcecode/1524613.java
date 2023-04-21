        public void write(Channel channel, Buffer data) {
            channel.onWrite.inQueue(data);
            channel.changeWriteState(OnWriting.getInstance());
            Context.getInstance().getChannelManager().onWriteRequired(channel);
        }
