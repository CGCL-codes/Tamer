    public static byte[] readBytesContent(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data[] = new byte[2048];
        int nbread = 0;
        while ((nbread = is.read(data)) > -1) {
            if (nbread > 0) {
                baos.write(data, 0, nbread);
            } else {
                try {
                    Thread.sleep(50);
                } catch (Exception ex) {
                    throw new IOException(ex.getMessage());
                }
            }
        }
        return baos.toByteArray();
    }
