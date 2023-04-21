    @Test(timeout = 10000)
    public void testRemoveDeadFiles() throws Exception {
        System.out.println("---- testRemoveDeadFiles() - start");
        File testDir = FileHelper.createTmpDir("test");
        try {
            client.connect(Inet4Address.getByName("localhost"), getPort());
            client.login(USER, PASSWORD);
            File[] files = createTestFiles(testDir);
            for (int i = 0; i < files.length; i++) {
                forceAddTrack(files[i].getAbsolutePath(), i, "Test " + i);
            }
            if (!files[0].delete() && files[0].exists()) {
                throw new IOException("Unable to delete file: " + files[0]);
            }
            recacheTracks();
            for (int i = 0; i < files.length; i++) {
                client.sendCommand(ITunesRemoteClient.CMD_FILE + ":" + files[i].getAbsolutePath(), 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
            }
            client.sendCommand(ITunesRemoteClient.CMD_REMOVE_DEAD_FILES, 220, ITunesRemoteClient.NO_TIMEOUT);
            client.sendCommand(ITunesRemoteClient.CMD_HELO, 220, ITunesRemoteClient.DEFAULT_TIMEOUT);
            List<String> commandLog = getCommandLog();
            System.out.println("------------------------");
            for (String s : commandLog) {
                System.out.println(s);
            }
            System.out.println("------------------------");
            Assert.assertEquals(3, commandLog.size());
            int index = 0;
            Assert.assertEquals("getTrackCount() = 3", commandLog.get(index++));
            Assert.assertEquals("getTracks()", commandLog.get(index++));
            Assert.assertEquals("removeTracksFromLibrary(Location: '" + files[0] + "' - Database ID: " + 0 + " - Name: 'Test " + 0 + "' )", commandLog.get(index++));
        } finally {
            FileHelper.delete(testDir);
        }
        System.out.println("---- testRemoveDeadFiles() - done");
    }
