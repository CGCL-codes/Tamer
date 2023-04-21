    public void onWhois(WhoisEvent whoisEvent) {
        System.out.println("onWhois");
        assertEquals("onWhois(): userInfo.nick", "Scurvy", whoisEvent.getWhoisInfo().getUserInfo().getNick());
        assertEquals("onWhois(): userInfo.user", "~Scurvy", whoisEvent.getWhoisInfo().getUserInfo().getUser());
        assertEquals("onWhois(): userInfo.host", "pcp825822pcs.nrockv01.md.comcast.net", whoisEvent.getWhoisInfo().getUserInfo().getHostName());
        assertEquals("onWhois(): channels", "@#sharktest", whoisEvent.getWhoisInfo().getChannels()[0]);
        assertTrue("onWhois(): idletime", 1018611059 == whoisEvent.getWhoisInfo().getIdleTime());
        assertTrue("onWhois(): operator", !whoisEvent.getWhoisInfo().isOperator());
        assertEquals("onWhois(): real name", "Scurvy", whoisEvent.getWhoisInfo().getRealName());
        assertEquals("onWhois(): server", "irc.sventech.com", whoisEvent.getWhoisInfo().getServer());
        assertEquals("onWhois(): server description", "GIMPnet IRC Server", whoisEvent.getWhoisInfo().getServerDescription());
    }
