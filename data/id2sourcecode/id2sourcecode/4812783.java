    public void profile() {
        try {
            JPLFastaReader fastaScanner = new JPLFastaReader(fastaFile);
            String nextSequence = "";
            while (fastaScanner.hasNext()) {
                try {
                    nextSequence = fastaScanner.nextFastaSequence();
                    JPLIAASequence sequence = new JPLAASequence.Builder(nextSequence).build();
                    List<JPLIAASequence> peptides = null;
                    if (trypsin != null) {
                        trypsin.digest(sequence);
                        List<JPLIAASequence> trypticPeptides = trypsin.getDigests();
                        for (JPLIAASequence trypPepSeq : trypticPeptides) {
                            enzyme.digest(trypPepSeq);
                            peptides = enzyme.getDigests();
                            for (JPLIAASequence pepSeq : peptides) {
                                treatSeq(pepSeq);
                            }
                        }
                    } else {
                        enzyme.digest(sequence);
                        peptides = enzyme.getDigests();
                        for (JPLIAASequence pepSeq : peptides) {
                            treatSeq(pepSeq);
                        }
                    }
                } catch (JPLAASequenceBuilderException e) {
                    logger.error(e.getMessage() + " ( " + nextSequence + ")");
                } catch (JPLAAByteUndefinedException e) {
                    logger.error("In " + nextSequence + ":" + e.getMessage() + ": " + " byte mass undefined.");
                }
            }
        } catch (IOException e1) {
            logger.error(e1.getMessage() + ": " + fastaFile + " read error.");
        }
    }
