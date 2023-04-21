        private static String MakeKey() {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
                return digest.digest(Long.toBinaryString(System.currentTimeMillis()).getBytes()).toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }
