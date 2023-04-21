    public static byte[] md5Digest(File file) throws InputOutputException {
        try {
            InputStream input = null;
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                input = new BufferedInputStream(new FileInputStream(file));
                byte[] buff = new byte[10240];
                int countBytes = 0;
                while ((countBytes = input.read(buff)) > -1) {
                    md.update(buff, 0, countBytes);
                }
                return md.digest();
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        } catch (Exception e) {
            throw new InputOutputException(e, file);
        }
    }
