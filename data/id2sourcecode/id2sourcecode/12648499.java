    private static String computeKey(String applicationId, String owner) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest1 = md.digest(applicationId.getBytes());
            byte[] digest2 = md.digest(owner.getBytes());
            int[] license = new int[12];
            for (int i = 0; i < license.length; ++i) {
                license[i] = digest1[i] ^ digest2[i];
            }
            return bytesToHexString(license);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
