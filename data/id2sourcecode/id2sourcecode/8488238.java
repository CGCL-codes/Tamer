    @Test
    public void processResponse() {
        byte[] bytes = new byte[19];
        Hex.set2(bytes, 5, 2);
        Hex.set4(bytes, 7, 10);
        Hex.set2(bytes, 11, 20);
        Hex.set4(bytes, 13, 11);
        Hex.set2(bytes, 17, 21);
        CueSceneRead command = new CueSceneRead(1, 1);
        command.processResponse(bytes);
        ChannelChanges cc = command.getChannelChanges();
        assertEquals(cc.getString(), "10[20] 11[21] ");
    }
