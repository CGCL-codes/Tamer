    private Font.Builder loadSingleOTFForBuilding(InputStream is) throws IOException {
        MessageDigest digest = null;
        if (this.fingerprintFont()) {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("Unable to get requested message digest algorithm.", e);
            }
            DigestInputStream dis = new DigestInputStream(is, digest);
            is = dis;
        }
        Builder builder = Builder.getOTFBuilder(this, is);
        if (this.fingerprintFont()) {
            builder.setDigest(digest.digest());
        }
        return builder;
    }
