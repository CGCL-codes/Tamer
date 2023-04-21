    public String encode(String input) {
        try {
            byte[] hash = java.security.MessageDigest.getInstance("MD5").digest(input.getBytes());
            return Base64.encodeBytes(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
