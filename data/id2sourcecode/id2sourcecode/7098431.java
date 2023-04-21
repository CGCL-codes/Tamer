    @Test(timeout = 10000)
    public void testRefreshFiles() throws Exception {
        System.out.println("---- testRefreshFiles() - start");
        File testDir = FileHelper.createTmpDir("test");
        try {
            client.connect(Inet4Address.getByName("localhost"), getPort());
            client.login(USER, PASSWORD);
            File[] files = createTestFiles(testDir);
            for (int i = 0; i < files.length; i++) {
                forceAddTrack(files[i].getAbsolutePath(), i, "Test " + i);
            }
            recacheTracks();
            for (int i = 0; i < files.length; i++) {
                client.sendCommand(ITunesRemoteClient.CMD_FILE + ":" + files[i].getAbsolutePath(), 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
            }
            client.sendCommand(ITunesRemoteClient.CMD_REFRESH_FILES, 220, ITunesRemoteClient.NO_TIMEOUT);
            client.sendCommand(ITunesRemoteClient.CMD_HELO, 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
            List<String> commandLog = getCommandLog();
            for (String s : commandLog) {
                System.out.println(s);
            }
            Assert.assertEquals(5, commandLog.size());
            int index = 0;
            Assert.assertEquals("getTrackCount() = 3", commandLog.get(index++));
            Assert.assertEquals("getTracks()", commandLog.get(index++));
            for (int i = 0; i < files.length; i++) {
                Assert.assertEquals("refreshTracks(Location: '" + files[i] + "' - Database ID: " + i + " - Name: 'Test " + i + "' )", commandLog.get(index++));
            }
            for (int i = 0; i < files.length; i++) {
                client.sendCommand(ITunesRemoteClient.CMD_FILE + ":" + files[i].getAbsolutePath(), 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
            }
            client.sendCommand(ITunesRemoteClient.CMD_REMOVE_FILES, 220, ITunesRemoteClient.NO_TIMEOUT);
        } finally {
            FileHelper.delete(testDir);
        }
        System.out.println("---- testRefreshFiles() - done");
    }
