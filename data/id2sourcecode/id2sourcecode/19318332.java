    void createFASTAIndexFile(String fastaFileName, String fastaIndexFileName) throws Exception {
        RandomAccessFile fastaFile = null;
        RandomAccessFile fastaFileIndex = null;
        long startCreateIndexTime = System.currentTimeMillis();
        long endCreateIndexTime;
        try {
            fastaFile = new RandomAccessFile(fastaFileName, "r");
            fastaFileIndex = new RandomAccessFile(fastaIndexFileName, "rw");
            InFileChannelHandler fastaFileChannelHandler = new InFileChannelHandler(fastaFile.getChannel());
            OutFileChannelHandler fastaFileIndexChannelHandler = new OutFileChannelHandler(fastaFileIndex.getChannel());
            IndexingFASTAFileVisitor indexingVisitor = new IndexingFASTAFileVisitor(fastaFileIndexChannelHandler);
            FASTAFileWalker fastaWalker = new FASTAFileWalker(fastaFileChannelHandler, indexingVisitor);
            fastaWalker.traverseFile();
        } finally {
            if (fastaFile != null) {
                try {
                    fastaFile.close();
                } catch (Exception ignore) {
                }
            }
            if (fastaFileIndex != null) {
                try {
                    fastaFileIndex.close();
                } catch (Exception ignore) {
                }
            }
            endCreateIndexTime = System.currentTimeMillis();
        }
        logger.debug("Finished creating FASTA index file " + fastaIndexFileName + " in " + (endCreateIndexTime - startCreateIndexTime) + "ms");
    }
