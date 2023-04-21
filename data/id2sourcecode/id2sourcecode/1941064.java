    public static final byte[] fileToByteArray(File f) {
        try {
            long len = f.length();
            byte[] bytes = new byte[(int) len];
            FileInputStream fin = new FileInputStream(f);
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = fin.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + f.getName());
            }
            fin.close();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }
