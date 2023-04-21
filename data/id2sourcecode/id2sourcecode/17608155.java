    protected void assertValuesMatch(SCFChromatogramFile scf, ZTRChromatogramFile ztr) {
        assertEquals(ztr.getBasecalls(), scf.getBasecalls());
        assertEquals(ztr.getPeaks(), scf.getPeaks());
        assertEquals(ztr.getQualities(), scf.getQualities());
        assertEquals(ztr.getChannelGroup(), scf.getChannelGroup());
        assertEquals(ztr.getNumberOfTracePositions(), scf.getNumberOfTracePositions());
    }
