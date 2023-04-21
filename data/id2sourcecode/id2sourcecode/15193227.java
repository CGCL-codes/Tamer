    @Override
    public boolean differs(final String fileName, final SortedMap<Integer, Integer> diffMap) throws FileIOException {
        try {
            if (opened) {
                fileChannel.position(0);
            } else {
                fileChannel = new FileInputStream(file).getChannel();
            }
        } catch (IOException exception) {
            throw HELPER_FILE_UTIL.fileIOException("failed differ " + file + " with " + fileName, file, exception);
        }
        boolean differs;
        FileChannel referenceChannel = null;
        try {
            referenceChannel = new FileInputStream(fileName).getChannel();
            if (fileChannel.size() == referenceChannel.size()) {
                final int length = 1024;
                final ByteBuffer byteBuffer = ByteBuffer.allocate(length);
                final ByteBuffer referenceByteBuffer = ByteBuffer.allocate(length);
                byte[] data;
                byte[] referenceData;
                int readPosition = 0;
                final DiffBuffer diffBuffer = new DiffBuffer(diffMap);
                differs = false;
                do {
                    data = read(byteBuffer, fileChannel);
                    referenceData = read(referenceByteBuffer, referenceChannel);
                    differs |= differs(data, referenceData, readPosition, diffBuffer);
                    readPosition += data.length;
                } while (data.length != 0);
                diffBuffer.close();
            } else {
                differs = true;
            }
        } catch (IOException exception) {
            throw HELPER_FILE_UTIL.fileIOException("failed differ " + file + " with " + fileName, file, exception);
        } finally {
            try {
                if (referenceChannel != null) {
                    referenceChannel.close();
                }
            } catch (Exception exception) {
            }
        }
        try {
            if (opened) {
                fileChannel.position(currentPositionInFile);
            } else {
                fileChannel.close();
            }
        } catch (IOException exception) {
            throw HELPER_FILE_UTIL.fileIOException("failed differ " + file + " with " + fileName, file, exception);
        }
        return differs;
    }
