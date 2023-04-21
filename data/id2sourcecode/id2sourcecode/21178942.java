    public void sendSystemMessage(String sysMsg) {
        if (session != null) {
            Channel channel = session.getChannel(StaticData.channel);
            channel.say(StaticData.clientMessage);
        }
    }
