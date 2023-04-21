    private Channel getChannel(Channel channel) {
        for (Iterator it = channels.iterator(); it.hasNext(); ) {
            Channel enteredChannel = (Channel) it.next();
            if (enteredChannel.equals(channel)) return enteredChannel;
        }
        return null;
    }
