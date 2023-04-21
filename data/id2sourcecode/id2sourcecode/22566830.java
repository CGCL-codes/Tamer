    public static synchronized String getPasswordHash(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            return "";
        }
        StringBuffer hash;
        try {
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));
            hash = new StringBuffer(hashBytes.length * 2);
            for (byte b : hashBytes) {
                String hex = String.format("%02X", b);
                hash.append(hex);
            }
        } catch (Exception e) {
            return "";
        }
        return hash.toString();
    }
