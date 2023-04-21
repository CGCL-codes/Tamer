    public void onInvite(InviteEvent inviteEvent) {
        System.out.println("onInvite");
        assertEquals("onInvite(): userInfo.getNick()", "Scurvy", inviteEvent.getUser().getNick());
        assertEquals("onInvite(): userInfo.getUser()", "~Scurvy", inviteEvent.getUser().getUser());
        assertEquals("onInvite(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", inviteEvent.getUser().getHostName());
        assertEquals("onInvite(): channel", "#sharktest", inviteEvent.getChannel());
    }
