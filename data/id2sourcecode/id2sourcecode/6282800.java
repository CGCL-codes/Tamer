    @Test
    public void testDigestionWithFilter() {
        final String sequenceString = "ASLLTAMRSAQISFK";
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        Transformer<DigestedPeptide, Integer> digestLength = new Transformer<DigestedPeptide, Integer>() {

            public Integer transform(DigestedPeptide digest) {
                return digest.length();
            }
        };
        Condition<DigestedPeptide> cond = new ConditionImpl.Builder<DigestedPeptide, Integer>(8).accessor(digestLength).operator(OperatorLowerThan.newInstance()).build();
        digester.setCondition(cond);
        digester.digest(sequence);
        Assert.assertEquals(1, digester.getDigests().size());
    }
