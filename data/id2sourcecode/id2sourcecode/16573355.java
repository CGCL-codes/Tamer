    private static Set<JPLIAASequence> makeTrypsinDigestion(String sequence) throws JPLEmptySequenceException, JPLParseException {
        JPLCleaver trypsin = new JPLCleaver(new JPLTrypsinKRnotPCutter());
        trypsin.digest(new JPLAASequence.Builder(sequence).build());
        return trypsin.getUniqueDigests();
    }
