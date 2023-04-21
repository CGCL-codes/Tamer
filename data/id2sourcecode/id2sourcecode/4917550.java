    public String encode(String plain) {
        try {
            byte[] strTemp = plain.getBytes();
            MessageDigest SHA = MessageDigest.getInstance("SHA");
            SHA.update(strTemp);
            byte[] sha = SHA.digest();
            return new String(sha);
        } catch (Exception e) {
            return null;
        }
    }
