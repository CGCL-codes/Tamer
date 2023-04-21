    public static byte[] readFile(File f) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(f);
            byte[] chunk = new byte[org.magnesia.Constants.CHUNK_SIZE];
            int read = 0;
            while ((read = fis.read(chunk)) > 0) {
                baos.write(chunk, 0, read);
            }
            fis.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
