    @Test(timeout = 10000)
    public void testClearFiles1() throws Exception {
        System.out.println("---- testClearFiles1() - start");
        client.connect(Inet4Address.getByName("localhost"), getPort());
        client.login(USER, PASSWORD);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah1", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah/blah2", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_CLEAR_FILES, 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_HELO, 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        List<String> commandLog = getCommandLog();
        Assert.assertEquals(0, commandLog.size());
        System.out.println("---- testClearFiles1() - done");
    }
