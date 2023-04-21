    public void onWho(WhoEvent whoEvent) {
        System.out.println("onWho");
        assertEquals("onWho(): userInfo.getNick()", "Scurvy", whoEvent.getUser().getNick());
        assertEquals("onWho(): userInfo.getUser()", "~Scurvy", whoEvent.getUser().getUser());
        assertEquals("onWho(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", whoEvent.getUser().getHostName());
        assertEquals("onWho(): channel", "#sharktest", whoEvent.getChannel());
        assertEquals("onWho(): ircServer", "irc.sventech.com", whoEvent.getIrcServer());
        assertEquals("onWho(): mask", "H@", whoEvent.getMask());
        assertEquals("onWho(): hopCount", 0, whoEvent.getHopCount());
        assertEquals("onWho(): real name", "Scurvy", whoEvent.getRealName());
        assertTrue("onWho(): last", !whoEvent.isLast());
    }
