    @Deprecated
    public org.restlet.data.Digest computeDigest(String algorithm) {
        org.restlet.data.Digest result = null;
        if (isAvailable()) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm);
                java.security.DigestInputStream dis = new java.security.DigestInputStream(getStream(), md);
                org.restlet.engine.io.BioUtils.exhaust(dis);
                result = new org.restlet.data.Digest(algorithm, md.digest());
            } catch (java.security.NoSuchAlgorithmException e) {
                Context.getCurrentLogger().log(Level.WARNING, "Unable to check the digest of the representation.", e);
            } catch (IOException e) {
                Context.getCurrentLogger().log(Level.WARNING, "Unable to check the digest of the representation.", e);
            }
        }
        return result;
    }
