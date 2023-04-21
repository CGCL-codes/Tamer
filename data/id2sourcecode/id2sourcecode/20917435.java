    @Test
    public void copyConstructor() {
        BasicChromatogram copy = new BasicChromatogram(sut);
        assertEquals(basecalls, copy.getBasecalls());
        assertEquals(mockPeaks, copy.getPeaks());
        assertEquals(mockChannelGroup, copy.getChannelGroup());
        assertEquals(expectedProperties, copy.getProperties());
    }
