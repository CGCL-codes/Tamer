    @Test
    public void abiVisitorMatchesZTR() throws FileNotFoundException, TraceDecoderException, IOException {
        BasicChromatogramFile actualAbi = new BasicChromatogramFile(id);
        Ab1FileParser.parse(RESOURCES.getFile("files/SDBHD01T00PB1A1672F.ab1"), actualAbi);
        assertEquals(expectedZTR.getNucleotideSequence(), actualAbi.getNucleotideSequence());
        assertEquals(expectedZTR.getPeaks(), actualAbi.getPeaks());
        assertEquals(expectedZTR.getQualities(), actualAbi.getQualities());
        assertEquals(expectedZTR.getChannelGroup(), actualAbi.getChannelGroup());
        assertEquals(expectedZTR.getNumberOfTracePositions(), actualAbi.getNumberOfTracePositions());
        assertEquals(expectedZTR.getComments(), actualAbi.getComments());
    }
