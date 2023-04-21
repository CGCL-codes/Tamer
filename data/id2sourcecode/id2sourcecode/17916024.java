    public static void scanNProcessFastaFile(String filename) throws IOException, JPLEmptySequenceException {
        JPLFastaReader fastaScanner = new JPLFastaReader(filename);
        int i = 0;
        int j = 0;
        while (fastaScanner.hasNext()) {
            String nextSequence = fastaScanner.nextFastaSequence();
            try {
                JPLIAASequence sequence = new JPLAASequence.Builder(nextSequence).build();
                if (logger.isDebugEnabled()) {
                    logger.debug("Trypsin digestion on " + sequence + "(" + sequence.length() + " aas)");
                }
                if (!sequence.isAmbiguous()) {
                    trypsin.digest(sequence);
                    List<JPLIAASequence> peptides = trypsin.getDigests();
                    if (logger.isInfoEnabled()) {
                        j += peptides.size();
                    }
                }
            } catch (JPLAASequenceBuilderException e) {
                if (logger.isDebugEnabled()) {
                    logger.warn(e.getMessage() + " ( " + nextSequence + ")");
                }
            }
            if (logger.isInfoEnabled()) {
                if (((i + 1) % 1000) == 0) {
                    logger.info((i + 1) + " fasta sequences red (" + (System.nanoTime() - startTime) + " ns.)");
                }
                i++;
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("Total of " + i + " fasta protein sequences red.");
            logger.info("Total of " + j + " digested peptides.");
        }
    }
