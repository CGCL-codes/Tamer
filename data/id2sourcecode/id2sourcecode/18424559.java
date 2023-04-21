    public static void writeFile(File file, byte[] data) throws IOException {
        final int MAX_BUFFER_SIZE = 4096;
        FileOutputStream output = null;
        FileChannel fc = null;
        try {
            output = new FileOutputStream(file);
            fc = output.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
            int offset = 0;
            while (offset < data.length) {
                buffer.clear();
                int len = data.length - offset;
                if (len > MAX_BUFFER_SIZE) len = MAX_BUFFER_SIZE;
                buffer.put(data, offset, len);
                offset += len;
                buffer.flip();
                fc.write(buffer);
            }
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }
