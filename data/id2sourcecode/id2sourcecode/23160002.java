    private void delete(long startPosition, long endPosition) throws IOException {
        if (io == null) setRewriteUnOpened();
        long recLen = endPosition - startPosition;
        ByteBuffer copy = ByteBuffer.allocate((int) recLen);
        FileChannel fc = io.getChannel();
        try {
            int readBytes = 0;
            if (endPosition <= startPosition || endPosition > io.length()) throw new InvalidKeyException();
            io.setLength(io.length() - recLen);
            do {
                io.seek(endPosition);
                if ((readBytes = fc.read(copy)) <= 0) break;
                fc.position(startPosition);
                copy.limit(readBytes);
                copy.position(0);
                fc.write(copy);
                if (io.getFilePointer() >= io.length()) break;
                endPosition += recLen;
                startPosition += recLen;
                copy.clear();
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
