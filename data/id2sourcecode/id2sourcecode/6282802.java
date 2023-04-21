    @Test
    public void testSeqOfPeptidasesDigestion() {
        final String sequenceString = "ASLLTAMDRSAQISFK";
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        final Peptidase aspn = Peptidase.getInstance("aspn", new CleavageSiteCutter.Builder("X|D").build());
        Digester digester1 = Digester.newInstance(aspn);
        Digester digester2 = Digester.newInstance(trypsin);
        digester1.digest(sequence);
        System.out.println("set of digests by aspn: " + digester1.getDigests());
        Assert.assertEquals(2, digester1.getDigests().size());
        Set<DigestedPeptide> digests = new HashSet<DigestedPeptide>();
        for (DigestedPeptide pep : digester1.getDigests()) {
            digester2.digest(pep.getPeptide());
            if (digester2.hasDigests()) {
                digests.addAll(digester2.getDigests());
            }
        }
        System.out.println("set of digests by aspn+trypsin: " + digests);
        Assert.assertEquals(2, digester1.getDigests().size());
    }
