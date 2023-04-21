    public Hash(byte[] bytes) {
        try {
            m_HashCode = MessageDigest.getInstance(DIGEST_TYPE).digest(bytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new CarabinerException("Error creating hash function for state", ex);
        }
    }
