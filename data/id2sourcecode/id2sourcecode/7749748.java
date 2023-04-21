    public void merge(final File inputDir, final File outputFile) throws IOException {
        if (maxMergeOnce == -1) {
            mergeAllAtOnce(inputDir, inputFileBufSize, outputFile, outputBufSize, reader, writer, comparator, deleteInputFiles);
        } else {
            mergeAll(inputDir, inputFileBufSize, outputFile, outputBufSize, reader, writer, comparator, deleteInputFiles, maxMergeOnce);
        }
    }
