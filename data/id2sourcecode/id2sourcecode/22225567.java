    @Override
    public void enterPortal(MapleClient c) {
        MapleCharacter player = c.getPlayer();
        double distanceSq = getPosition().distanceSq(player.getPosition());
        if (distanceSq > 22500) {
            player.getCheatTracker().registerOffense(CheatingOffense.USING_FARAWAY_PORTAL, "D" + Math.sqrt(distanceSq));
        }
        boolean changed = false;
        if (getScriptName() != null) {
            if (!FourthJobQuestsPortalHandler.handlePortal(getScriptName(), c.getPlayer())) {
                changed = PortalScriptManager.getInstance().executePortalScript(this, c);
            }
        } else if (getTargetMapId() != 999999999) {
            MapleMap to;
            if (player.getEventInstance() == null) {
                to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(getTargetMapId());
            } else {
                to = player.getEventInstance().getMapInstance(getTargetMapId());
            }
            MaplePortal pto = to.getPortal(getTarget());
            if (pto == null) {
                pto = to.getPortal(0);
            }
            c.getPlayer().changeMap(to, pto);
            changed = true;
        }
        if (!changed) {
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }
