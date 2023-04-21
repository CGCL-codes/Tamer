    private static Set<DigestedPeptide> makeDigestion(String sequence) throws ParseException {
        digester.digest(new Peptide.Builder(sequence).ambiguityEnabled().build());
        return digester.getDigests();
    }
