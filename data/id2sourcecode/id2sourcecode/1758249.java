    private static void copy(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] ba = new byte[BUF_SIZE];
            int n;
            while ((n = in.read(ba)) > 0) out.write(ba, 0, n);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
            try {
                out.close();
            } catch (IOException e) {
            }
        }
    }
