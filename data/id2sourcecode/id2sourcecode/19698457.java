    @Test
    public void testNumberOfCleavageSiteForAspN() throws JPLEmptySequenceException, JPLParseException {
        JPLCleaver enzyme = new JPLCleaver(new JPLRegExpCutter("(?=D)"));
        String sequenceString = "DFVESNTIFNLNTVK";
        JPLIAASequence sequence = new JPLAASequence.Builder(sequenceString).build();
        enzyme.digest(sequence);
        assertEquals(0, enzyme.getNumOfCleavageSites(), 0);
    }
