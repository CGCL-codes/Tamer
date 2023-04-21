    private String readStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[8192];
            for (; ; ) {
                int read = in.read(buf);
                if (read <= 0) break;
                out.write(buf, 0, read);
            }
            return out.toString(gspEncoding);
        } finally {
            out.close();
            in.close();
        }
    }
