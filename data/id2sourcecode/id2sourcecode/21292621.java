    private void verifyParser(SCFChromatogramBuilder c, long currentOffset, DataInputStream in, long skipDistance) throws SectionDecoderException {
        replay(mockHeader);
        long newOffset = sut.getHandler().decode(in, currentOffset, mockHeader, c);
        verify(mockHeader);
        SCFChromatogram chromatogram = sut.getChromatogram();
        assertEquals(newOffset - currentOffset - skipDistance, (int) bases.getLength() * 12);
        assertEquals(chromatogram.getNucleotideSequence().asList(), c.basecalls().asList());
        Sequence<ShortSymbol> encodedPeaks = new EncodedSequence<ShortSymbol>(PEAK_CODEC, PEAKS_FACTORY.getGlyphsFor(c.peaks()));
        assertEquals(chromatogram.getPeaks().getData(), encodedPeaks);
        assertArrayEquals(chromatogram.getChannelGroup().getAChannel().getConfidence().getData(), c.aConfidence());
        assertArrayEquals(chromatogram.getChannelGroup().getCChannel().getConfidence().getData(), c.cConfidence());
        assertArrayEquals(chromatogram.getChannelGroup().getGChannel().getConfidence().getData(), c.gConfidence());
        assertArrayEquals(chromatogram.getChannelGroup().getTChannel().getConfidence().getData(), c.tConfidence());
        assertOptionalConfidenceEqual(chromatogram.getSubstitutionConfidence(), new DefaultConfidence(c.substitutionConfidence()));
        assertOptionalConfidenceEqual(chromatogram.getInsertionConfidence(), new DefaultConfidence(c.insertionConfidence()));
        assertOptionalConfidenceEqual(chromatogram.getDeletionConfidence(), new DefaultConfidence(c.deletionConfidence()));
    }
