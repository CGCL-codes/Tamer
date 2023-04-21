    public Set<DigestedPeptide> makeDigestion(Peptide peptide, Digester digester) throws ParseException {
        digester.digest(peptide);
        return digester.getDigests();
    }
