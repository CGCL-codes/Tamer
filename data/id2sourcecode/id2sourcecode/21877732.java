    public void onPublicNotice(PublicNoticeEvent publicNoticeEvent) {
        System.out.println("onPublicNotice");
        assertEquals("onPublicNotice(): userInfo.getNick()", "Scurvy", publicNoticeEvent.getUser().getNick());
        assertEquals("onPublicNotice(): userInfo.getUser()", "~Scurvy", publicNoticeEvent.getUser().getUser());
        assertEquals("onPublicNotice(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", publicNoticeEvent.getUser().getHostName());
        assertEquals("onPublicNotice(): channel", "#sharktest", publicNoticeEvent.getChannel());
        assertEquals("onPublicNotice(): notice", "public notice", publicNoticeEvent.getNotice());
    }
