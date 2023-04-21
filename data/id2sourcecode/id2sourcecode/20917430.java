    @Test
    public void constructionWithEmptyProperties() {
        BasicChromatogram emptyProps = new BasicChromatogram(basecalls, qualities, mockPeaks, mockChannelGroup);
        assertEquals(basecalls, emptyProps.getBasecalls());
        assertEquals(mockPeaks, emptyProps.getPeaks());
        assertEquals(mockChannelGroup, emptyProps.getChannelGroup());
        assertEquals(qualities, sut.getQualities());
        assertEquals(new Properties(), emptyProps.getProperties());
    }
