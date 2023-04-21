    @Test
    public void testNumberOfCleavageSiteFromTrypsin() {
        final String sequenceString = "ASLLTAMRSAQISFK";
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        digester.digest(sequence);
        assertEquals(1, digester.getNumOfCleavageSites(), 0);
    }
