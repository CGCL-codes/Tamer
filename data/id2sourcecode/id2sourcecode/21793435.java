    public static final String getMD5(File f) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] buf = new byte[65536];
        int num_read;
        InputStream in = new BufferedInputStream(new FileInputStream(f));
        while ((num_read = in.read(buf)) != -1) {
            m.update(buf, 0, num_read);
        }
        String result = new BigInteger(1, m.digest()).toString(16);
        if (result.length() < 32) {
            int paddingSize = 32 - result.length();
            for (int i = 0; i < paddingSize; i++) result = "0" + result;
        }
        System.out.println("MD5: " + result);
        return result;
    }
