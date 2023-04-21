    public void doEvent() {
        if (eventOn()) {
            this.warp(getPlayer().getClient().getChannelServer().eventMap);
            getClient().getSession().write(MaplePacketCreator.serverNotice(5, "You have been warped to the event map!"));
        }
    }
