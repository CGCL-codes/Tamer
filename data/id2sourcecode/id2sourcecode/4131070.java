    public void testSizes() {
        byte[] data = new byte[idata.length];
        for (int i = 0; i < idata.length; i++) data[i] = (byte) idata[i];
        WavBuffer w = new WavBuffer(data);
        Assert.assertTrue("sample rate", 11025.0 == w.getSampleRate());
        Assert.assertEquals("sample size", 8, w.getSampleSizeInBits());
        Assert.assertEquals("channels   ", 1, w.getChannels());
    }
