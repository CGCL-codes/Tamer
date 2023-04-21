    @Test
    public void testNumberOfCleavageSite() {
        final Peptidase enzyme = Peptidase.getInstance("enz", new CleavageSiteCutter.Builder("[DE]|X").build());
        final String sequenceString = "ASLLTAMSDAQISFD";
        digester = Digester.newInstance(enzyme);
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        digester.digest(sequence);
        assertEquals(1, digester.getNumOfCleavageSites(), 0);
    }
