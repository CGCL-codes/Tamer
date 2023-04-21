    public static void writeAceTags(AceTags aceTags, OutputStream out) throws IOException {
        for (ReadAceTag readTag : aceTags.getReadTags()) {
            writeReadTag(readTag, out);
        }
        for (ConsensusAceTag consensusTag : aceTags.getConsensusTags()) {
            writeConsensusTag(consensusTag, out);
        }
        for (WholeAssemblyAceTag wholeAssemblyTag : aceTags.getWholeAssemblyTags()) {
            writeWholeAssemblyTag(wholeAssemblyTag, out);
        }
    }
