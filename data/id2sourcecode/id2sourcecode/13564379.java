    public static final void sCopyAll(InputStream iIS, OutputStream iOS) {
        byte[] buffer = new byte[4096];
        for (; ; ) {
            int readLength = 0;
            try {
                if (0 == iIS.available()) {
                    readLength = iIS.read(buffer, 0, 1);
                    if (readLength == -1) break;
                }
                int available = iIS.available();
                if (available > 0) {
                    if (available > buffer.length - readLength) available = buffer.length - readLength;
                    int nextChunk = iIS.read(buffer, readLength, available);
                    if (nextChunk > 0) readLength += nextChunk;
                }
            } catch (Exception ex) {
                break;
            }
            try {
                iOS.write(buffer, 0, readLength);
            } catch (Exception ex) {
                break;
            }
        }
        try {
            iOS.close();
        } catch (Exception ex) {
        }
        try {
            iIS.close();
        } catch (Exception ex) {
        }
    }
