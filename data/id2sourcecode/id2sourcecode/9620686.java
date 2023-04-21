    @Test
    public void testAddChannel() {
        RecorderModel recorderModel = new RecorderModel();
        MockChannelDriver channelDriver = new MockChannelDriver(1, 0);
        recorderModel.addChannel("channel 1", 1000);
        channelDriver.collectData(recorderModel.getChannelByName("channel 1"));
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            System.err.println(ex);
            fail();
        }
        assertEquals("channel 1", recorderModel.getChannelByName("channel 1").getName());
        assertTrue(recorderModel.getChannelByName("channel 1").scaleDeepCopy(0, 1, 1).getScaled().length > 3);
    }
