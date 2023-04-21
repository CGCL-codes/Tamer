    @Test
    public void constructor() {
        assertEquals(basecalls, sut.getNucleotideSequence());
        assertEquals(mockPeaks, sut.getPeaks());
        assertEquals(mockChannelGroup, sut.getChannelGroup());
        assertEquals(expectedProperties, sut.getComments());
        assertEquals(mockInsertionConfidence, sut.getInsertionConfidence());
        assertEquals(mockDeletionConfidence, sut.getDeletionConfidence());
        assertEquals(mockSubstitutionConfidence, sut.getSubstitutionConfidence());
        assertEquals(mockPrivateData, sut.getPrivateData());
        assertEquals(qualities, sut.getQualities());
    }
