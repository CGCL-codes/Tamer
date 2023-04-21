    private String makePassword(String password) {
        try {
            String encryptedPassword = "*";
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(md.digest(password.getBytes()));
            for (int i = 0; i < digest.length; i++) {
                String s = Integer.toHexString(digest[i] & 0xFF).toUpperCase();
                if (s.length() == 1) {
                    s = "0" + s;
                }
                encryptedPassword = encryptedPassword + s;
            }
            return encryptedPassword;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No algorithm. FIXME");
        }
        return null;
    }
