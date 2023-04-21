    public boolean isIdentityValid(final Identity id) {
        if (id == null) {
            return false;
        }
        final String uName = id.getUniqueName();
        final String puKey = id.getPublicKey();
        try {
            final String given_digest = uName.substring(uName.indexOf("@") + 1, uName.length()).trim();
            String calculatedDigest = Core.getCrypto().digest(puKey.trim()).trim();
            calculatedDigest = Mixed.makeFilename(calculatedDigest).trim();
            if (!given_digest.equals(calculatedDigest)) {
                logger.severe("Warning: public key of sharer didn't match its digest:\n" + "given digest :'" + given_digest + "'\n" + "pubkey       :'" + puKey.trim() + "'\n" + "calc. digest :'" + calculatedDigest + "'");
                return false;
            }
        } catch (final Throwable e) {
            logger.log(Level.SEVERE, "Exception during key validation", e);
            return false;
        }
        return true;
    }
