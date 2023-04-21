    public static byte[] readBytes(URL url) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            in = url.openStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            do {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            } while (count != -1);
            return out.toByteArray();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
