    private String md5(String str) throws NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("MD5").digest(str.getBytes());
        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }
