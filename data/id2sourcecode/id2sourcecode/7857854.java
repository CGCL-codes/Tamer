    public void onPart(PartEvent partEvent) {
        System.out.println("onPart");
        assertEquals("onPart(): userInfo.getNick()", "Scurvy", partEvent.getUser().getNick());
        assertEquals("onPart(): userInfo.getUser()", "~Scurvy", partEvent.getUser().getUser());
        assertEquals("onPart(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", partEvent.getUser().getHostName());
        assertEquals("onPart(): channel", "#sharktest", partEvent.getChannel());
        assertEquals("onPart(): reason", "Later all", partEvent.getReason());
    }
