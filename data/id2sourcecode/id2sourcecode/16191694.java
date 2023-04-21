    public static String sha1(String input) {
        if (input != null) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA1");
                md.reset();
                byte[] buffer = input.getBytes();
                md.update(buffer);
                byte[] digest = md.digest();
                StringBuilder hexStr = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    hexStr.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
                }
                return hexStr.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
