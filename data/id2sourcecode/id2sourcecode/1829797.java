    private void commitFileData(boolean syncHeaderToo) {
        if (currentFileRa == null) return;
        try {
            if (enableDebugOutput) {
                System.out.println(String.format("Flushing data to file \"%s\".", currentFilename.getName()));
            }
            currentFileRa.getChannel().force(true);
            assert (long) currentFileSize == currentFileRa.getFilePointer() : ErrUtils.assertionFailed();
            assert (long) currentFileSize == currentFileRa.getChannel().size();
            if (currentFileSize < SetOfTestDataFiles.MAX_HEADER_LEN) {
                return;
            }
            if (enableDebugOutput) {
                System.out.println(String.format("Writing an updated header to file \"%s\" with a size of %d bytes.", currentFilename.getName(), currentFileSize));
            }
            long savedPos = currentFileRa.getFilePointer();
            currentFileRa.seek(0);
            SetOfTestDataFiles.writeHeader(currentFileNumber, currentFileSize, testDataGenerator, currentFileRandomDataSeed, currentFilename, dataBuffer, currentFileRa);
            currentFileRa.seek(savedPos);
            if (syncHeaderToo) {
                if (enableDebugOutput) {
                    System.out.println(String.format("Flushing header to file \"%s\".", currentFilename.getName()));
                }
                currentFileRa.getChannel().force(true);
            }
        } catch (Throwable ex) {
            throw ErrUtils.asRuntimeExceptionWithFilename(ex, currentFilename, ErrFileOp.FE_FLUSHING);
        }
    }
