    @Test(timeout = 10000)
    public void testQuitLoggedIn() throws Exception {
        System.out.println("---- testQuitLoggedIn() - start");
        client.connect(Inet4Address.getByName("localhost"), getPort());
        client.login(USER, PASSWORD);
        client.sendCommand(ITunesRemoteClient.CMD_QUIT, 221, ITunesRemoteClient.DEFAULT_TIMEOUT);
        System.out.println("---- testQuitLoggedIn() - done");
    }
