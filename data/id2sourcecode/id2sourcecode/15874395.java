    public static int write(File baseTarget, InputStream input, long inputLength, int chunkSize, boolean useBase64) throws IOException {
        final int headerSize = getHeaderSize(useBase64);
        final int dataChunkSize;
        if (useBase64) {
            final int maxDataChunkSize = chunkSize - headerSize;
            final int rejected = maxDataChunkSize % 3;
            dataChunkSize = maxDataChunkSize - rejected;
        } else dataChunkSize = chunkSize - headerSize;
        if (dataChunkSize <= 0) throw new IllegalArgumentException("chunkSize is smaller or equal than the header size.");
        final int lastChunkIndex = (inputLength % dataChunkSize == 0) ? (int) (inputLength / dataChunkSize) - 1 : (int) (inputLength / dataChunkSize);
        if (lastChunkIndex >= 65536) throw new IllegalArgumentException("chunkSize is so small that more than 65536 files would be generated. This is not supported.");
        MessageDigest inputDigest;
        try {
            inputDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        OutputFile[] outputFiles = new OutputFile[lastChunkIndex + 1];
        MessageDigest[] chunkDigests = new MessageDigest[lastChunkIndex + 1];
        int[] offsets = new int[lastChunkIndex + 1];
        byte[] dataBytes = new byte[4096];
        int cumulatedSize = 0;
        for (int chunkIndex = 0; chunkIndex <= lastChunkIndex; chunkIndex++) {
            File outputFile = new File(baseTarget.getCanonicalPath() + chunkIndex);
            outputFiles[chunkIndex] = new OutputFile(outputFile, useBase64);
            try {
                chunkDigests[chunkIndex] = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            offsets[chunkIndex] = cumulatedSize;
            final int dataSize = (int) ((chunkIndex == lastChunkIndex) ? inputLength - cumulatedSize : dataChunkSize);
            cumulatedSize += dataSize;
            int remaining = dataSize;
            while (remaining != 0) {
                int toRead = Math.min(remaining, dataBytes.length);
                int nRead = input.read(dataBytes, 0, toRead);
                if (-1 == nRead) {
                    throw new EOFException("End of stream reached but at least " + remaining + " additional bytes were expected.");
                }
                outputFiles[chunkIndex].write(dataBytes, 0, nRead);
                chunkDigests[chunkIndex].update(dataBytes, 0, nRead);
                inputDigest.update(dataBytes, 0, nRead);
                remaining -= nRead;
            }
            if (useBase64 && (chunkIndex == lastChunkIndex)) {
                final int padCnt = (3 - (dataSize % 3)) % 3;
                if (padCnt > 0) {
                    byte[] padding = new byte[padCnt];
                    outputFiles[chunkIndex].write(padding, 0, padCnt);
                    chunkDigests[chunkIndex].update(padding, 0, padCnt);
                    inputDigest.update(padding, 0, padCnt);
                }
            }
        }
        byte[] fileHash = inputDigest.digest();
        if (FILEHASH_SIZE != fileHash.length) throw new RuntimeException("32!=fileHash.length. fileHash.length=" + fileHash.length);
        if (inputLength != cumulatedSize) throw new RuntimeException("inputLength!=cumulatedSize: inputLength=" + inputLength + ", cumulatedSize=" + cumulatedSize);
        cumulatedSize = 0;
        for (int chunkIndex = 0; chunkIndex <= lastChunkIndex; chunkIndex++) {
            int thisChunkDataSize = (int) ((chunkIndex != lastChunkIndex) ? dataChunkSize : inputLength - cumulatedSize);
            cumulatedSize += thisChunkDataSize;
            byte[] header = new byte[MultiPartStore.getHeaderDataAndPadCnt(useBase64)];
            System.arraycopy(fileHash, 0, header, 0, fileHash.length);
            AArrayUtilities.int2Bytes((int) inputLength, header, FILESIZE_OFFSET - 1);
            AArrayUtilities.int2Bytes(offsets[chunkIndex], header, DATAOFFSET_OFFSET - 1);
            AArrayUtilities.int2Bytes(thisChunkDataSize, header, DATASIZE_OFFSET - 1);
            byte[] dataHash = chunkDigests[chunkIndex].digest();
            System.arraycopy(dataHash, 0, header, DATAHASH_OFFSET - 1, DATAHASH_SIZE);
            outputFiles[chunkIndex].writeHeaderAndClose(header);
        }
        return lastChunkIndex + 1;
    }
