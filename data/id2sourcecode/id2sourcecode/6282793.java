    @Test
    public void testDigestedPeptideAmbiguity() {
        Peptide protein = new Peptide.Builder("MQRSTATGCFKXL").ambiguityEnabled().cterm(CTerminus.PROT_C).nterm(NTerminus.PROT_N).build();
        digester.digest(protein);
        Assert.assertTrue(protein.isAmbiguous());
        Iterator<DigestedPeptide> iter = digester.getDigests().iterator();
        boolean isAmb = false;
        while (iter.hasNext()) {
            DigestedPeptide digest = iter.next();
            if (digest.isAmbiguous()) {
                isAmb = true;
            }
        }
        Assert.assertTrue(isAmb);
    }
