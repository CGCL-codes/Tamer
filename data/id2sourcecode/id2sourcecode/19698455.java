    @Test
    public void testOutOfBoundDigestion() throws JPLEmptySequenceException, JPLParseException {
        JPLCleaver enzyme = new JPLCleaver(new JPLRegExpCutter("(?<=[DE])"));
        String sequenceString = "MASRKLRDQIVIATKFTTDYKGYDVGKGKSANFCGNHKRSLHVSVRDSLRKLQTDWIDIL" + "YVHWWDYMSSIEEVMDSLHILVQQGKVLYLGVSDTPAWVVSAANYYATSHGKTPFSIYQG" + "KWNVLNRDFERDIIPMARHFGMALAPWDVMGGGRFQSKKAVEERKKKGEGLRTFFGTSEQ" + "TDMEVKISEALLKVAEEHGTESVTAIAIAYVRSKAKHVFPLVGGRKIEHLKQNIEALSIK" + "LTPEQIKYLESIVPFDVGFPTNFIGDDPAVTKKPSFLTEMSAKISFED";
        JPLIAASequence sequence = new JPLAASequence.Builder(sequenceString).build();
        enzyme.digest(sequence);
        List<JPLIAASequence> digests = enzyme.getDigests();
        logger.info(digests);
    }
