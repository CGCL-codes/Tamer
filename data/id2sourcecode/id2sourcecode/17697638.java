    static byte[] toByteArray(File f) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream((int) f.length());
        FileInputStream fis = new FileInputStream(f);
        try {
            byte[] buf = new byte[1024 * 16];
            int read;
            while ((read = fis.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
            baos.flush();
        } finally {
            baos.close();
            fis.close();
        }
        return baos.toByteArray();
    }
