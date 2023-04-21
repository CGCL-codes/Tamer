    private static final void Copy(InputStream in, OutputStream out) {
        try {
            try {
                byte[] iob = new byte[128];
                int read;
                while (0 < (read = in.read(iob, 0, 128))) out.write(iob, 0, read);
            } finally {
                in.close();
            }
        } catch (IOException exc) {
        }
    }
