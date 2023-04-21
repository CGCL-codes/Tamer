    private byte[] readStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[2048];
            int read = 0;
            while (in.available() > 0) {
                read = in.read(buffer, 0, buffer.length);
                if (read < 0) {
                    break;
                }
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } finally {
            out.close();
        }
    }
