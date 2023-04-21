    public static void scanNProcessFastaFile(final String filename) throws IOException, JPLParseException {
        if (logger.isInfoEnabled()) {
            logger.info("open filename " + filename);
        }
        final JPLFastaReader reader = JPLFastaReader.newInstance();
        reader.parse(new File(filename));
        Iterator<JPLFastaEntry> it = reader.iterator();
        int i = 0;
        int j = 0;
        while (it.hasNext()) {
            final String nextSequence = it.next().getSequence();
            final JPLPeptide sequence = new JPLPeptide.Builder(nextSequence).ambiguityEnabled().build();
            if (logger.isDebugEnabled()) {
                logger.debug("Trypsin digestion on " + sequence + "(" + sequence.length() + " aas)");
            }
            if (!sequence.isAmbiguous()) {
                digester.digest(sequence);
                final Set<JPLDigestedPeptide> peptides = digester.getDigests();
                if (logger.isInfoEnabled()) {
                    j += peptides.size();
                }
                fragmentAllPeptides(peptides);
            }
            if (logger.isInfoEnabled()) {
                if (((i + 1) % 100) == 0) {
                    logger.info((i + 1) + " fasta sequences red (" + (System.nanoTime() - startTime) + " ns.)");
                }
                i++;
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("Total of " + i + " fasta protein sequences red.");
            logger.info("Total of " + j + " digested peptides (avg size:" + (meanPeptideSize / j) + " aas)");
            logger.info("Total of " + fragmentNumberGenerated + " generated fragments.");
        }
    }
