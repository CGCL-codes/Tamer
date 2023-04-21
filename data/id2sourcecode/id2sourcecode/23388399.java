    public static byte[] bytesFrom(InputStream in) throws IOException {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream(4096);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) != -1) str.write(buffer, 0, length);
            return str.toByteArray();
        } finally {
            close(in);
        }
    }
