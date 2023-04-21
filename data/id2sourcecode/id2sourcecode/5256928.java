    public String getEncryptedResourceID() {
        messageDigest.update(resourceID.getBytes(), 0, resourceID.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }
