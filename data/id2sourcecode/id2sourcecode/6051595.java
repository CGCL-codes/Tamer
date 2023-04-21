    public String generateMD5(byte[] data) {
        if (data != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(data);
                return new String(encoder.encode(md.digest()));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
