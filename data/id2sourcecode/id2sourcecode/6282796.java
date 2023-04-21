    @Test
    public void testOutOfBoundDigestion() {
        final Peptidase enzyme = Peptidase.getInstance("enz", new CleavageSiteCutter.Builder("[DE]|X").build());
        digester = Digester.newInstance(enzyme);
        final String sequenceString = "MASRKLRDQIVIATKFTTDYKGYDVGKGKSANFCGNHKRSLHVSVRDSLRKLQTDWIDIL" + "YVHWWDYMSSIEEVMDSLHILVQQGKVLYLGVSDTPAWVVSAANYYATSHGKTPFSIYQG" + "KWNVLNRDFERDIIPMARHFGMALAPWDVMGGGRFQSKKAVEERKKKGEGLRTFFGTSEQ" + "TDMEVKISEALLKVAEEHGTESVTAIAIAYVRSKAKHVFPLVGGRKIEHLKQNIEALSIK" + "LTPEQIKYLESIVPFDVGFPTNFIGDDPAVTKKPSFLTEMSAKISFED";
        final Peptide sequence = new Peptide.Builder(sequenceString).build();
        digester.digest(sequence);
        final Set<DigestedPeptide> digests = digester.getDigests();
        System.out.println(digests);
    }
