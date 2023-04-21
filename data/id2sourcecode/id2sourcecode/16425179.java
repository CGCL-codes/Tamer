    private static void writeAssembledFromRecords(AcePlacedRead read, long fullLength, OutputStream out) throws IOException {
        AssembledFrom assembledFrom = AssembledFrom.createFrom(read, fullLength);
        writeString(AceFileUtil.createAssembledFromRecord(assembledFrom), out);
    }
