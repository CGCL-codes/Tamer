    public static byte[] readFileIntoByteArray(String pathname) {
        byte[] bytes = null;
        try {
            File file = new File(pathname);
            InputStream is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new RuntimeException("File is too large to be stored in a byte array");
            }
            bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }
