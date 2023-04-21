    @Override
    public void dropMessage(String message) {
        client.getSession().write(MaplePacketCreator.getWhisper(whisperfrom, client.getChannel(), message));
    }
