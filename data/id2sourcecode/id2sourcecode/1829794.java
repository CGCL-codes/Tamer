    private void openLastFile() {
        if (discoveredFiles.isClean()) {
            return;
        }
        try {
            currentFilename = discoveredFiles.lastFilename;
            currentFileRa = new RandomAccessFile(discoveredFiles.lastFilename, "rw");
            if (currentFileRa.getChannel().size() != discoveredFiles.lastFileRealSize) {
                throw new RuntimeException("File size mismatch.");
            }
            currentProgress.currentFilename = currentFilename.getAbsolutePath();
            currentFileNumber = discoveredFiles.lastFileNumber;
            if (discoveredFiles.lastFileCommittedSize < SetOfTestDataFiles.MAX_HEADER_LEN) {
                currentFileRandomDataSeed = SetOfTestDataFiles.SMALL_FILE_SEED;
            } else {
                currentFileRandomDataSeed = discoveredFiles.lastFileSeed;
            }
            if (discoveredFiles.lastFileCommittedSize == discoveredFiles.lastFileRealSize) {
                if (enableDebugOutput) {
                    System.out.println(String.format("The last file \"%2$s\" has a total of %1$d committed bytes.", discoveredFiles.lastFileCommittedSize, currentFilename.getName()));
                }
                currentFileRa.seek(discoveredFiles.lastFileCommittedSize);
                currentFileSize = discoveredFiles.lastFileRealSize;
                return;
            }
            if (discoveredFiles.lastFileCommittedSize > discoveredFiles.lastFileRealSize) {
                throw new RuntimeException("The file contents are corrupt.");
            }
        } catch (Throwable ex) {
            throw ErrUtils.asRuntimeException(ex, String.format("Error opening the last test data file \"%s\": ", discoveredFiles.lastFilename));
        }
        try {
            int truncatedSize = discoveredFiles.lastFileRealSize - discoveredFiles.lastFileCommittedSize;
            if (enableDebugOutput) {
                System.out.println(String.format("Truncating %1$d bytes from file \"%2$s\".", truncatedSize, currentFilename.getName()));
            }
            currentFileRa.setLength(discoveredFiles.lastFileCommittedSize);
            currentFileRa.seek(discoveredFiles.lastFileCommittedSize);
            currentFileSize = discoveredFiles.lastFileCommittedSize;
        } catch (Throwable ex) {
            throw ErrUtils.asRuntimeException(ex, String.format("Error truncating non-committed data at the end of \"%s\": ", discoveredFiles.lastFilename));
        }
    }
