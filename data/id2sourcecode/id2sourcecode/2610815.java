    private String getCacheFileName(URI uri) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = uri.toString().getBytes();
            bytes = md.digest(bytes);
            String fn = "";
            for (int i = 0; i < 16; i++) {
                fn += String.format("%02x", bytes[i]);
            }
            return fn;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to create MD5 message digest object.", e);
        }
    }
