    public void setConnectedUsers() {
        Channel currentChannel = session.getChannel(StaticData.channel);
        List<String> connectedUsers = currentChannel.getNicks();
        List<String> activeUsers = new ArrayList<String>();
    }
