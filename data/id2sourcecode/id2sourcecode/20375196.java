    public static String generateBranchId() {
        String b = new Integer((int) (Math.random() * 10000)).toString() + System.currentTimeMillis();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte bid[] = messageDigest.digest(b.getBytes());
            return BRANCH_MAGIC_COOKIE + toHexString(bid);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
