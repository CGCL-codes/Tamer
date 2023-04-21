    public Id store(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("null inputStream");
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
        Id insecureId = delegateCache.store(digestInputStream);
        SecureId secureId = new SecureId(algorithm, messageDigest.digest());
        delegateIdMap.put(secureId, insecureId);
        return secureId;
    }
