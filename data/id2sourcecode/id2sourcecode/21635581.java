    private static String hashPassword(String password) {
        try {
            byte[] HexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            byte[] hashBytes = MessageDigest.getInstance("SHA-512").digest(password.getBytes());
            StringBuilder str = new StringBuilder(2 * hashBytes.length);
            for (int i = 0; i < hashBytes.length; i++) {
                int v = hashBytes[i] & 0xff;
                str.append((char) HexChars[v >> 4]);
                str.append((char) HexChars[v & 0xf]);
            }
            return str.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
