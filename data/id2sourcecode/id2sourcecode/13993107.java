    public static String hashPassword(String unhashedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hashedPassword = "";
            byte[] encrypted = md.digest(unhashedPassword.getBytes());
            for (byte b : encrypted) hashedPassword += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
