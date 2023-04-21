    @Test
    public void constructionWithEmptyProperties() {
        BasicChromatogram emptyProps = new BasicChromatogram(id, basecalls, qualities, mockPeaks, mockChannelGroup);
        assertEquals(basecalls, emptyProps.getNucleotideSequence());
        assertEquals(mockPeaks, emptyProps.getPeaks());
        assertEquals(mockChannelGroup, emptyProps.getChannelGroup());
        assertEquals(qualities, sut.getQualities());
        assertEquals(new Properties(), emptyProps.getComments());
    }
