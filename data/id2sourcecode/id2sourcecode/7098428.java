    @Test(timeout = 10000)
    public void testAddFiles() throws Exception {
        System.out.println("---- testAddFiles() - start");
        client.connect(Inet4Address.getByName("localhost"), getPort());
        client.login(USER, PASSWORD);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah1", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah/blah2", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_ADD_FILES, 220, ITunesRemoteClient.NO_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_HELO, 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        List<String> commandLog = getCommandLog();
        Assert.assertEquals(3, commandLog.size());
        Assert.assertEquals("addFilesToLibrary(/blah)", commandLog.get(0));
        Assert.assertEquals("addFilesToLibrary(/blah1)", commandLog.get(1));
        Assert.assertEquals("addFilesToLibrary(/blah/blah2)", commandLog.get(2));
        System.out.println("---- testAddFiles() - done");
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah1", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_FILE + ":/blah/blah2", 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
        client.sendCommand(ITunesRemoteClient.CMD_REMOVE_FILES, 220, ITunesRemoteClient.NO_TIMEOUT);
    }
