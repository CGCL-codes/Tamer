    public static String hash(File f) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("Unknown algorithm: MD5");
            }
            byte[] buffer = new byte[16384];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            return toHexString(md.digest());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
