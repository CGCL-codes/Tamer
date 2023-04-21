    protected void assertValuesMatch(SCFChromatogram scf, ZTRChromatogram ztr) {
        assertEquals(ztr.getNucleotideSequence(), scf.getNucleotideSequence());
        assertEquals(ztr.getPeaks(), scf.getPeaks());
        assertEquals(ztr.getQualities(), scf.getQualities());
        assertEquals(ztr.getChannelGroup(), scf.getChannelGroup());
        assertEquals(ztr.getNumberOfTracePositions(), scf.getNumberOfTracePositions());
    }
