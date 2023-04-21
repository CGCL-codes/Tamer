    @Test
    public void testNumberOfCleavageSite() throws JPLEmptySequenceException, JPLParseException {
        JPLCleaver enzyme = new JPLCleaver(new JPLRegExpCutter("(?<=[DE])"));
        String sequenceString = "ASLLTAMSDAQISFD";
        JPLIAASequence sequence = new JPLAASequence.Builder(sequenceString).build();
        enzyme.digest(sequence);
        assertEquals(1, enzyme.getNumOfCleavageSites(), 0);
    }
