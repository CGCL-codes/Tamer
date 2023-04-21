    long searchSequencePos(String fastaIndexFileName, String accessionNo, FASTASequenceCache fastaSequenceCache) throws Exception {
        long seqPos = -1;
        if (FileUtil.fileExists(fastaIndexFileName)) {
            RandomAccessFile fastaFileIndex = null;
            try {
                fastaFileIndex = new RandomAccessFile(fastaIndexFileName, "r");
                InFileChannelHandler fastaFileIndexChannelHandler = new InFileChannelHandler(fastaFileIndex.getChannel());
                ReadFASTAIndexHandler fastaIndex = new ReadFASTAIndexHandler(fastaFileIndexChannelHandler, fastaSequenceCache);
                if (fastaIndex.verifySignature()) {
                    seqPos = fastaIndex.searchId(accessionNo);
                }
                fastaIndex.close();
            } finally {
                if (fastaFileIndex != null) {
                    try {
                        fastaFileIndex.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        }
        return seqPos;
    }
