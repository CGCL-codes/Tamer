    public void scanNProcessFastaFile(String filename) throws IOException, JPLEmptySequenceException {
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
                    fragmentAllPeptides(peptides);
                } else {
                    logger.warn(sequence + " has ambiguities");
                }
            } catch (JPLAASequenceBuilderException e) {
                if (logger.isDebugEnabled()) {
                    logger.warn(e.getMessage() + " ( " + nextSequence + ")");
                }
            }
            if (logger.isInfoEnabled()) {
                if (((i + 1) % 10000) == 0) {
                    logger.info((i + 1) + " fasta sequences red (" + (System.nanoTime() - startTime) + " ns.)");
                }
                if (((j + 1) % 10000) == 0) {
                    logger.info((j + 1) + " digests peptides sequences.");
                }
                if (((fragmentNumberGenerated + 1) % 10000) == 0) {
                    logger.info((i + 1) + " fasta sequences red.");
                    logger.info((fragmentNumberGenerated + 1) + " fragmented ions sequences.");
                }
                i++;
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("Total of " + i + " fasta protein sequences red.");
            logger.info("Total of " + j + " digested peptides (avg size:" + (meanPeptideSize / j) + " aas)");
            logger.info("Total of " + fragmentNumberGenerated + " generated fragments.");
            if (enableFragmentationRedundancyStat) {
                logger.info(numOfRedundantPeptide2Fragment + "/" + numOfPeptides + " redondant fragmentation operations (" + (numOfRedundantPeptide2Fragment * 100. / numOfPeptides) + "% )");
            }
        }
        Assert.assertEquals(6, i);
        Assert.assertEquals(284, j);
        Assert.assertEquals(2372, (int) meanPeptideSize);
        Assert.assertEquals(7416, fragmentNumberGenerated);
        Assert.assertEquals(11, numOfRedundantPeptide2Fragment);
        Assert.assertEquals(141, numOfPeptides);
    }
