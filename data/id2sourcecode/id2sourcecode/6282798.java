    @Test
    public void testNumberOfCleavageSiteForAspN() {
        final Peptidase enzyme = Peptidase.getInstance("aspn", new CleavageSiteCutter.Builder("X|D").build());
        final String sequenceString = "DFVESNTIFNLNTVK";
        digester = Digester.newInstance(enzyme);
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        digester.digest(sequence);
        assertEquals(0, digester.getNumOfCleavageSites(), 0);
    }
