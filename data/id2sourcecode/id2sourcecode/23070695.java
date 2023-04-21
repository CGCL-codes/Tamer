    @Test
    public void testGetChannelData() throws IOException, BadParameterException {
        for (int i = 0; i < 100; i++) assertEquals(mono.getChannelData(0)[i], i, 0.001);
        for (int i = 0; i < 150; i++) {
            assertEquals(stereo.getChannelData(0)[i], 2 * i, 0.001);
            assertEquals(stereo.getChannelData(1)[i], 3 * i, 0.001);
        }
    }
