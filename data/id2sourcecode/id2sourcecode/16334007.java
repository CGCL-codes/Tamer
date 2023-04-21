    private byte[] digestPhoto(byte[] photoFile) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA1 error: " + e.getMessage(), e);
        }
        byte[] photoDigest = messageDigest.digest(photoFile);
        return photoDigest;
    }
