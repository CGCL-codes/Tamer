    @BeforeTest
    public void init() {
        createSession();
        addServerInfo(ServerInfo.HYPERION);
        addChannel("#test");
        chan = session.getChannel("#test");
    }
