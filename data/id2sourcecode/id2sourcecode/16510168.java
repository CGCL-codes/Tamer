    public static byte[] readRAM(final InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream collect = new ByteArrayOutputStream(512);
        try {
            int read = 0;
            do {
                read = in.read(buffer);
                if (read > 0) {
                    collect.write(buffer, 0, read);
                }
            } while (read != -1);
            return collect.toByteArray();
        } finally {
            in.close();
        }
    }
