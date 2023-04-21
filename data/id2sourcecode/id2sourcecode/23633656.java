    protected byte[] engineDoFinal() {
        if (initOnly) {
            throw new java.lang.IllegalStateException("No HMAC operations permitted" + " when the engine has been started with the initOnly argument.");
        }
        if (firstPass) {
            messageDigest.update(inner);
        }
        firstPass = !firstPass;
        byte[] digest = messageDigest.digest();
        if (preInit) {
            preInit = false;
            macSupport.secondaryInit(outer);
        } else {
            messageDigest.update(outer);
        }
        messageDigest.update(digest);
        try {
            messageDigest.digest(digest, 0, digest.length);
        } catch (DigestException ex) {
        }
        return !truncated ? digest : getTruncatedDigest(digest);
    }
