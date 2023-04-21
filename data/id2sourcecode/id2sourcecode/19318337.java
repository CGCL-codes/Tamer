    Map<String, Long> searchSequencesPos(String fastaIndexFileName, Set<String> accessions) throws Exception {
        Map<String, Long> accessionsPosMap = new LinkedHashMap<String, Long>();
        if (FileUtil.fileExists(fastaIndexFileName)) {
            RandomAccessFile fastaFileIndex = null;
            try {
                fastaFileIndex = new RandomAccessFile(fastaIndexFileName, "r");
                InFileChannelHandler fastaFileIndexChannelHandler = new InFileChannelHandler(fastaFileIndex.getChannel());
                ReadFASTAIndexHandler fastaIndex = new ReadFASTAIndexHandler(fastaFileIndexChannelHandler, null);
                if (fastaIndex.verifySignature()) {
                    fastaIndex.searchIds(accessions, accessionsPosMap);
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
        for (String id : accessions) {
            accessionsPosMap.put(id, -1L);
        }
        return accessionsPosMap;
    }
