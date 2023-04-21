    public static String getMD5Checksum(File file) throws IOException {
        try {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] buf = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    md.update(buf, 0, len);
                }
                return bytesToHexString(md.digest());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(MessageFormat.format(Messages.getString("FileHelper.UNABLE_GET_CHECKSUM_FILE"), file), e);
        }
    }
