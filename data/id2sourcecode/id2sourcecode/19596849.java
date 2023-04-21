    public Channel getChannelParameter(int i) {
        Channel channel = null;
        String param = getParameter(i);
        try {
            int num = Integer.parseInt(param) - 1;
            channel = ChannelManager.getInstance().getChannel(num);
        } catch (NumberFormatException e) {
            channel = ChannelManager.getInstance().getChannel(param, true);
        }
        return channel;
    }
