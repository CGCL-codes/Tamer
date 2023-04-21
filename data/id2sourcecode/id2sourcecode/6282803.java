    @Test
    public void testCnbrDigest() {
        final String sequenceString = "AHNGMRWPTG";
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        Peptidase cnbr = Peptidase.getInstance("CNBr");
        digester = Digester.newInstance(cnbr);
        digester.digest(sequence);
        Set<DigestedPeptide> digests = digester.getDigests();
        System.out.println(digests);
        assertEquals(1, digester.getNumOfCleavageSites(), 0);
    }
