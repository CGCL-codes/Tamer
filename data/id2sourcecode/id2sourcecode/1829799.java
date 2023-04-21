    private void addTestData(long bytesToAdd) {
        try {
            assert bytesToAdd > 0 : ErrUtils.assertionFailed();
            assert currentFileSize < SetOfTestDataFiles.MAX_FILE_SIZE : ErrUtils.assertionFailed();
            assert currentFileRa.getFilePointer() == currentFileSize : ErrUtils.assertionFailed();
            assert currentFileRa.length() == currentFileSize : ErrUtils.assertionFailed();
            assert currentFilename.length() == currentFileSize : ErrUtils.assertionFailed();
            int spaceLeftInFile = SetOfTestDataFiles.MAX_FILE_SIZE - currentFileSize;
            long maxBs = Common.calculateMaxBlockSize(currentProgress.targetDataSize);
            int blockSize = Math.min((int) Math.min(spaceLeftInFile, maxBs), (int) Math.min(bytesToAdd, dataBuffer.length));
            final boolean writeOneByteAtATimeForDebuggingPurposes = false;
            if (writeOneByteAtATimeForDebuggingPurposes) blockSize = 1;
            assert blockSize > 0 : ErrUtils.assertionFailed();
            int bytesAdded;
            if (currentFileSize < SetOfTestDataFiles.MAX_HEADER_LEN && currentFileSize + blockSize >= SetOfTestDataFiles.MAX_HEADER_LEN) {
                assert currentFileRandomDataSeed == SetOfTestDataFiles.SMALL_FILE_SEED : ErrUtils.assertionFailed();
                currentFileRandomDataSeed = rndGenerator.nextInt();
                if (enableDebugOutput) {
                    System.out.println(String.format("Writing first header to file \"%s\".", currentFilename.getName()));
                }
                currentFileRa.seek(0);
                SetOfTestDataFiles.writeHeader(currentFileNumber, SetOfTestDataFiles.MAX_HEADER_LEN, testDataGenerator, currentFileRandomDataSeed, currentFilename, dataBuffer, currentFileRa);
                currentFileRa.getChannel().force(true);
                assert currentFileRa.getFilePointer() == SetOfTestDataFiles.MAX_HEADER_LEN : ErrUtils.assertionFailed();
                bytesAdded = SetOfTestDataFiles.MAX_HEADER_LEN - currentFileSize;
                assert bytesAdded >= 0 && bytesAdded <= SetOfTestDataFiles.MAX_HEADER_LEN : ErrUtils.assertionFailed();
            } else {
                int alignedBlockSize = testDataGenerator.alignDataSize(currentFileSize, blockSize);
                if (enableDebugOutput) {
                    String msg = String.format("Writing %1$d data bytes to file \"%2$s\".", alignedBlockSize, currentFilename.getName());
                    System.out.println(msg);
                }
                testDataGenerator.generateData(currentFileNumber, currentFileSize, currentFileRandomDataSeed, alignedBlockSize, dataBuffer);
                currentFileRa.write(dataBuffer, 0, alignedBlockSize);
                bytesAdded = alignedBlockSize;
            }
            currentFileSize += bytesAdded;
            currentProgress.currentDataSize += bytesAdded;
            currentProgress.processedDataSize += bytesAdded;
        } catch (Throwable ex) {
            throw ErrUtils.asRuntimeExceptionWithFilename(ex, currentFilename, ErrFileOp.FE_WRITING);
        }
    }
