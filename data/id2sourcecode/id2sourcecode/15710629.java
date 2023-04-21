    public static byte[] readAvailable(InputStream is) throws IOException {
        if (is != null) {
            int available = is.available();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(available);
            byte[] readed = new byte[available];
            while (available > 0) {
                int read_bytes = is.read(readed);
                if (read_bytes >= 0) {
                    baos.write(readed, 0, read_bytes);
                    available = is.available();
                } else {
                    break;
                }
            }
            return baos.toByteArray();
        }
        return null;
    }
