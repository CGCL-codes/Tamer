    private void readData(OutputStream stream) throws IOException {
        int boundaryLength = boundary.length();
        int read = 0;
        boolean reachedEnd = false;
        while (!reachedEnd) {
            if (read > 1) {
                parameterBuffer[0] = parameterBuffer[read - 2];
                parameterBuffer[1] = parameterBuffer[read - 1];
                read = 2;
            }
            while (!reachedEnd && (parameterBuffer.length - read - boundaryLength - 4) >= 0) {
                int num = lineInput.readLine(parameterBuffer, read, parameterBuffer.length - read);
                if (num == -1) throw new IOException("premature EOF"); else if (num - 2 == boundaryLength) {
                    boolean matched = true;
                    for (int i = 0; matched && (i < boundaryLength); i++) {
                        matched &= boundaryBytes[i] == parameterBuffer[read + i];
                    }
                    if (matched) {
                        reachedEnd = true;
                    } else read += num;
                } else if (num - 4 == boundaryLength) {
                    boolean matched = true;
                    for (int i = 0; matched && (i < boundaryLength); i++) {
                        matched &= boundaryBytes[i] == parameterBuffer[read + i];
                        if (!matched) ;
                    }
                    if (matched && (parameterBuffer[read + boundaryLength] == '-') && parameterBuffer[read + boundaryLength + 1] == '-' && parameterBuffer[read + boundaryLength + 2] == '\r') {
                        reachedEnd = true;
                        eof = true;
                    } else read += num;
                } else read += num;
            }
            if (read > 2) {
                stream.write(parameterBuffer, 0, read - 2);
            }
        }
        stream.close();
    }
