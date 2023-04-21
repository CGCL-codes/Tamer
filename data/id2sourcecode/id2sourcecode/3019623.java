    @Test
    public void fullConstructor() {
        assertEquals(basecalls, sut.getNucleotideSequence());
        assertEquals(mockPeaks, sut.getPeaks());
        assertEquals(mockChannelGroup, sut.getChannelGroup());
        assertEquals(expectedProperties, sut.getComments());
        assertEquals(qualities, sut.getQualities());
    }
