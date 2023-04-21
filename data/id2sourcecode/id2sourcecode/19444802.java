    public synchronized Vector getChannelsContainingUser(String nick) {
        Vector result = new Vector();
        for (int i = 0; i < mChannels.size(); i++) {
            IrcChannel channel = (IrcChannel) mChannels.elementAt(i);
            if (channel.findUser(nick) != null) {
                result.addElement(channel);
            }
        }
        return result;
    }
