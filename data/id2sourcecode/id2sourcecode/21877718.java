    public void onKick(KickEvent kickEvent) {
        System.out.println("onKick");
        assertEquals("onKick(): userInfo.getNick()", "Scurvy", kickEvent.getUser().getNick());
        assertEquals("onKick(): userInfo.getUser()", "~Scurvy", kickEvent.getUser().getUser());
        assertEquals("onKick(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", kickEvent.getUser().getHostName());
        assertEquals("onKick(): channel", "#sharktest", kickEvent.getChannel());
        assertEquals("onKick(): kickee", "alphaX234", kickEvent.getKickee());
        assertEquals("onKick(): reason", "Go away", kickEvent.getReason());
    }
