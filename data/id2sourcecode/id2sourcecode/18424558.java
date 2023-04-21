    public static void readFile(File file, OutputStream output) throws IOException {
        FileInputStream input = null;
        FileChannel fc = null;
        try {
            input = new FileInputStream(file);
            fc = input.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            for (; ; ) {
                buffer.clear();
                int n = fc.read(buffer);
                if (n == (-1)) break;
                output.write(buffer.array(), 0, buffer.position());
            }
            output.flush();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }
