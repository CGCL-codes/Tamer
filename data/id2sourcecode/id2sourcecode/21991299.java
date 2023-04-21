    private static java.nio.ByteBuffer readFileToBuffer(java.io.File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        try {
            FileChannel fc = is.getChannel();
            java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate((int) fc.size());
            for (int count = 0; count >= 0 && buffer.hasRemaining(); ) {
                count = fc.read(buffer);
            }
            buffer.flip();
            return buffer;
        } finally {
            is.close();
        }
    }
