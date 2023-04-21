    @Test
    public void testNumberOfCleavageSiteFromTrypsin() throws JPLEmptySequenceException, JPLParseException {
        String sequenceString = "ASLLTAMRSAQISFK";
        JPLIAASequence sequence = new JPLAASequence.Builder(sequenceString).build();
        regexTrypsin.digest(sequence);
        assertEquals(1, regexTrypsin.getNumOfCleavageSites(), 0);
    }
