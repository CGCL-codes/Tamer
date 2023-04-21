    private boolean isPossibleOut(Channel c, String name) {
        if (c.getName().equals(name)) {
            return false;
        }
        Send[] sends = c.getSends();
        for (int i = 0; i < sends.length; i++) {
            if (sends[i].getSendChannel().equals(name)) {
                return false;
            }
            String sendChannelName = sends[i].getSendChannel();
            if (!sendChannelName.equals(Channel.MASTER)) {
                Channel next = getChannelByName(sendChannelName);
                if (!isPossibleOut(next, name)) {
                    return false;
                }
            }
        }
        String outChannel = c.getOutChannel();
        if (outChannel.equals(name)) {
            return false;
        }
        if (outChannel.equals(Channel.MASTER)) {
            return true;
        }
        Channel next = getChannelByName(outChannel);
        return isPossibleOut(next, name);
    }
