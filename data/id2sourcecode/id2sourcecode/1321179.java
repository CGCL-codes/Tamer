    private byte[] generateCommitHash(String pass) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance(COMMIT_ALGORITHM).digest(pass.getBytes());
        } catch (NoSuchAlgorithmException e) {
            handleException(e, null);
        } finally {
            return hash;
        }
    }
