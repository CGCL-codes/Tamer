    public void onAction(ActionEvent actionEvent) {
        System.out.println("onAction");
        assertEquals("onAction(): userInfo.getNick()", "Scurvy", actionEvent.getUserInfo().getNick());
        assertEquals("onAction(): userInfo.getUser()", "~Scurvy", actionEvent.getUserInfo().getUser());
        assertEquals("onAction(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", actionEvent.getUserInfo().getHostName());
        assertEquals("onAction(): channel", "#sharktest", actionEvent.getChannel());
        assertEquals("onAction(): description", "Looks around", actionEvent.getDescription());
    }
