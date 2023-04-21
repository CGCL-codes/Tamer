    public static String generateBranchId() {
        StringBuffer ret = new StringBuffer();
        StringBuffer b = new StringBuffer();
        String hex;
        b.append(Integer.toString((int) (Math.random() * 10000)));
        b.append(System.currentTimeMillis());
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = messageDigest.digest(b.toString().getBytes());
            hex = toHexString(bytes);
        } catch (NoSuchAlgorithmException ex) {
            hex = "NoSuchAlgorithmExceptionMD5";
        }
        ret.append(BRANCH_MAGIC_COOKIE + hex);
        return ret.toString();
    }
