    public static String MD5File(File file) {
        try {
            if (file.isFile()) {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[8192];
                int read = 0;
                try {
                    while ((read = is.read(buffer)) > 0) {
                        digest.update(buffer, 0, read);
                    }
                    byte[] md5sum = digest.digest();
                    BigInteger bigInt = new BigInteger(1, md5sum);
                    String md5 = bigInt.toString(16);
                    if (md5.length() == 31) {
                        md5 = "0" + md5;
                    }
                    return md5;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
