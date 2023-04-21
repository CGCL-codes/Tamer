    public void onJoin(JoinEvent joinEvent) {
        System.out.println("onJoin");
        assertEquals("onJoin(): userInfo.getNick()", "alphaX234", joinEvent.getUser().getNick());
        assertEquals("onJoin(): userInfo.getUser()", "~alphaX234", joinEvent.getUser().getUser());
        assertEquals("onJoin(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", joinEvent.getUser().getHostName());
        assertEquals("onJoin(): channel", "#sharktest", joinEvent.getChannel());
    }
