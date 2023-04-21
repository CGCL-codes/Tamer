    int readWireFormat(InputStream in, byte[] buffer, int bufferIndex) throws IOException {
        int start = headerStart = bufferIndex;
        if (in.read(buffer, bufferIndex, HEADER_LENGTH) != HEADER_LENGTH) {
            throw new IOException("unexpected EOF reading smb header");
        }
        bufferIndex += readHeaderWireFormat(buffer, bufferIndex);
        if ((wordCount = in.read()) == -1) {
            throw new IOException("unexpected EOF reading smb wordCount");
        }
        buffer[bufferIndex++] = (byte) (wordCount & 0xFF);
        if (wordCount != 0) {
            if (in.read(buffer, bufferIndex, wordCount * 2) != wordCount * 2) {
                throw new IOException("unexpected EOF reading smb parameter words");
            }
            int n;
            if ((n = readParameterWordsWireFormat(buffer, bufferIndex)) != wordCount * 2) {
                if (DebugFile.trace) {
                    DebugFile.writeln("wordCount * 2=" + (wordCount * 2) + " but readParameterWordsWireFormat returned " + n);
                }
            }
            bufferIndex += wordCount * 2;
        }
        if (in.read(buffer, bufferIndex, 2) != 2) {
            throw new IOException("unexpected EOF reading smb byteCount");
        }
        byteCount = readInt2(buffer, bufferIndex);
        bufferIndex += 2;
        if (byteCount != 0) {
            if (in.read(buffer, bufferIndex, byteCount) != byteCount) {
                throw new IOException("unexpected EOF reading smb");
            }
            int n;
            if ((n = readBytesWireFormat(buffer, bufferIndex)) != byteCount) {
                if (DebugFile.trace) {
                    DebugFile.writeln("byteCount=" + byteCount + " but readBytesWireFormat returned " + n);
                }
            }
            bufferIndex += byteCount;
        }
        length = bufferIndex - start;
        return length;
    }
