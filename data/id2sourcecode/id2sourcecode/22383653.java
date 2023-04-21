    public String randomSHA1() {
        byte[] b = new byte[4];
        int i = ranGen.nextInt();
        for (int j = 0; j < 4; j++) {
            b[j] = (byte) (i % 256);
            i >>= 8;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        byte[] chksum = md.digest(b);
        StringBuffer result = new StringBuffer();
        for (int j = 0; j < chksum.length; j++) result.append(Integer.toHexString(0xFF & chksum[j]));
        return result.toString();
    }
