    public BaseSequenceEntity readSequence(FastaFileNode fastaFileNode, String accessionNo) throws Exception {
        BaseSequenceEntity sequenceEntity = null;
        RandomAccessFile fastaFile = null;
        long startReadSequenceTime = System.currentTimeMillis();
        long endReadSequenceTime;
        try {
            String fastaFileLocation = fastaFileNode.getDirectoryPath();
            if (!FileUtil.fileExists(fastaFileLocation)) {
                throw new IllegalArgumentException("FASTA file location " + "'" + fastaFileLocation + "'" + " not found for " + fastaFileNode.getObjectId());
            }
            FASTASequenceCache fastaSequenceCache = null;
            if (fastaNodesCache != null) {
                fastaSequenceCache = fastaNodesCache.get(fastaFileNode.getObjectId());
                if (fastaSequenceCache == null) {
                    fastaSequenceCache = new FASTASequenceCache();
                    fastaNodesCache.put(fastaFileNode.getObjectId(), fastaSequenceCache);
                }
                sequenceEntity = fastaSequenceCache.getSequenceEntity(accessionNo);
            }
            if (sequenceEntity == null) {
                long seqPos = -1;
                if (fastaSequenceCache != null) {
                    fastaSequenceCache.getSequenceEntityPos(accessionNo);
                }
                if (seqPos < 0) {
                    seqPos = searchSequencePos(fastaFileNode.getFastaIndexFilePath(), accessionNo, fastaSequenceCache);
                }
                fastaFile = new RandomAccessFile(fastaFileNode.getFastaFilePath(), "r");
                InFileChannelHandler fastaFileChannelHandler = new InFileChannelHandler(fastaFile.getChannel());
                if (seqPos > 0) {
                    fastaFileChannelHandler.setPosition(seqPos);
                }
                sequenceEntity = readSequence(fastaFileChannelHandler, accessionNo, fastaSequenceCache);
            }
        } finally {
            if (fastaFile != null) {
                try {
                    fastaFile.close();
                } catch (Exception ignore) {
                }
            }
            endReadSequenceTime = System.currentTimeMillis();
        }
        logger.debug("Finished reading sequence for " + accessionNo + " in " + (endReadSequenceTime - startReadSequenceTime) + "ms");
        return sequenceEntity;
    }
