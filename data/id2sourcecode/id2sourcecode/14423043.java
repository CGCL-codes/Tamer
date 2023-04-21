    public String digest(String clearText) {
        try {
            if (clearText == null) {
                return null;
            }
            MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
            byte[] digest = md.digest(clearText.getBytes());
            return new String(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error("digest: " + digestAlgorithm + " algorithm not found", e);
        }
        return null;
    }
