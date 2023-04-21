    public void onPublic(PublicMessageEvent publicMessageEvent) {
        System.out.println("onPublic");
        assertEquals("onPublic(): userInfo.getNick()", "Scurvy", publicMessageEvent.getUser().getNick());
        assertEquals("onPublic(): userInfo.getUser()", "~Scurvy", publicMessageEvent.getUser().getUser());
        assertEquals("onPublic(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", publicMessageEvent.getUser().getHostName());
        assertEquals("onPublic(): channel", "#sharktest", publicMessageEvent.getChannel());
        assertEquals("onPublic(): message", "  Test  of public-message!", publicMessageEvent.getMessage());
    }
