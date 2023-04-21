    public void reset(byte[] K) throws java.security.NoSuchAlgorithmException {
        if (null != K) {
            this.KH.reset();
            this.KH.update(K);
            byte[] md = this.KH.digest();
            this.K = java.security.SecureRandom.getInstance(this.KN);
            this.K.setSeed(md);
        } else throw new java.lang.IllegalArgumentException();
    }
