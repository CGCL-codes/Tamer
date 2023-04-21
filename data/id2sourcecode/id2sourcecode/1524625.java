        public void interestWrite(Channel channel) throws ChannelClosedException {
            Context.getInstance().getChannelManager().onWriteRequired(channel);
        }
