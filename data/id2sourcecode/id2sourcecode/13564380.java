    public static final long sCopy(InputStream iIS, OutputStream iOS, long iCount) {
        long countWritten = 0;
        final byte[] buffer = new byte[4096];
        while (iCount != 0) {
            int readLength = 0;
            try {
                if (0 == iIS.available()) {
                    readLength = iIS.read(buffer, 0, 1);
                    if (readLength == -1) break;
                }
                int available = iIS.available();
                if (available > 0) {
                    if (available > buffer.length - readLength) available = buffer.length - readLength;
                    if (available > iCount - readLength) available = (int) (iCount - readLength);
                    int nextChunk = iIS.read(buffer, readLength, available);
                    if (nextChunk > 0) readLength += nextChunk;
                }
                iCount -= readLength;
            } catch (Exception ex) {
                break;
            }
            try {
                iOS.write(buffer, 0, readLength);
            } catch (Exception ex) {
                break;
            }
            countWritten += readLength;
        }
        return countWritten;
    }
