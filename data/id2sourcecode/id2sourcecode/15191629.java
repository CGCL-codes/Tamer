    public final String getBuildId() {
        byte[] bytes = new byte[2048];
        InputStream is = null;
        MessageDigest md5 = null;
        int nRead = 0;
        try {
            md5 = MessageDigest.getInstance("MD5");
            if ((is = context.getResourceAsStream(buildJSPath)) != null) {
                while ((nRead = is.read(bytes)) > 0) {
                    md5.update(bytes, 0, nRead);
                }
            }
        } catch (Exception e) {
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        if (md5 == null) {
            return null;
        }
        return new BigInteger(1, md5.digest()).toString(16);
    }
