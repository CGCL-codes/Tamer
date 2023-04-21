    public static String hashPassword(String clear) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hash = md.digest(clear.getBytes());
            StringBuffer sb = new StringBuffer();
            int i;
            for (i = 0; i < hash.length; i++) {
                sb.append(HEXTABLE[(hash[i] >> 4) & 0xf]);
                sb.append(HEXTABLE[hash[i] & 0xf]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.severe("Got: " + e);
            return null;
        }
    }
