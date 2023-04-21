    public static void writeAceFile(AceAssembly<AceContig> aceAssembly, SliceMapFactory sliceMapFactory, OutputStream out, boolean calculateBestSegments) throws IOException, DataStoreException {
        int numberOfContigs = 0;
        int numberOfReads = 0;
        DataStore<AceContig> aceDataStore = aceAssembly.getContigDataStore();
        for (Contig<AcePlacedRead> contig : aceDataStore) {
            numberOfContigs++;
            numberOfReads += contig.getNumberOfReads();
        }
        try {
            writeString(String.format("AS %d %d%n%n", numberOfContigs, numberOfReads), out);
            PhdDataStore phdDataStore = aceAssembly.getPhdDataStore();
            for (AceContig contig : aceDataStore) {
                if (calculateBestSegments) {
                    SliceMap sliceMap = sliceMapFactory.createNewSliceMap(new DefaultCoverageMap.Builder(contig.getPlacedReads()).build(), aceAssembly.getQualityDataStore());
                    AceFileWriter.writeAceFile(contig, sliceMap, phdDataStore, out, calculateBestSegments);
                } else {
                    AceFileWriter.writeAceFile(contig, phdDataStore, out);
                }
            }
            AceTagMap aceTagMap = aceAssembly.getAceTagMap();
            if (aceTagMap != null) {
                for (ReadAceTag readTag : aceTagMap.getReadTags()) {
                    writeReadTag(readTag, out);
                }
                for (ConsensusAceTag consensusTag : aceTagMap.getConsensusTags()) {
                    writeConsensusTag(consensusTag, out);
                }
                for (WholeAssemblyAceTag wholeAssemblyTag : aceTagMap.getWholeAssemblyTags()) {
                    writeWholeAssemblyTag(wholeAssemblyTag, out);
                }
            }
        } finally {
            IOUtil.closeAndIgnoreErrors(out);
        }
    }
