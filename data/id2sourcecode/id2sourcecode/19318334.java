    Set<BaseSequenceEntity> readSequences(String fastaFileName, String fastaFileIndexName, Set<String> accessions) throws Exception {
        Set<BaseSequenceEntity> sequenceEntities = null;
        RandomAccessFile fastaFile = null;
        long startReadSequenceTime = System.currentTimeMillis();
        long endReadSequenceTime;
        int nSequences = -1;
        try {
            Map<String, Long> seqPosMap = null;
            if (accessions != null) {
                nSequences = accessions.size();
                seqPosMap = searchSequencesPos(fastaFileIndexName, accessions);
            }
            fastaFile = new RandomAccessFile(fastaFileName, "r");
            InFileChannelHandler fastaFileChannelHandler = new InFileChannelHandler(fastaFile.getChannel());
            sequenceEntities = readSequences(fastaFileChannelHandler, seqPosMap);
        } finally {
            if (fastaFile != null) {
                try {
                    fastaFile.close();
                } catch (Exception ignore) {
                }
            }
            endReadSequenceTime = System.currentTimeMillis();
        }
        logger.debug("Finished reading " + (nSequences >= 0 ? nSequences : "all") + " sequences in " + (endReadSequenceTime - startReadSequenceTime) + "ms");
        return sequenceEntities;
    }
