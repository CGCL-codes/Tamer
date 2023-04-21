    @Test
    public void fullConstructor() {
        assertEquals(basecalls, sut.getBasecalls());
        assertEquals(mockPeaks, sut.getPeaks());
        assertEquals(mockChannelGroup, sut.getChannelGroup());
        assertEquals(expectedProperties, sut.getProperties());
        assertEquals(qualities, sut.getQualities());
    }
