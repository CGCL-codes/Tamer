    public byte[] passwordToKey(OctetString passwordString, byte[] engineID) {
        MessageDigest md = getDigestObject();
        byte[] digest;
        byte[] buf = new byte[64];
        int password_index = 0;
        int count = 0;
        byte[] password = passwordString.getValue();
        while (count < 1048576) {
            for (int i = 0; i < 64; ++i) {
                buf[i] = password[password_index++ % password.length];
            }
            md.update(buf);
            count += 64;
        }
        digest = md.digest();
        if (logger.isDebugEnabled()) {
            logger.debug(protoName + "First digest: " + new OctetString(digest).toHexString());
        }
        md.reset();
        md.update(digest);
        md.update(engineID);
        md.update(digest);
        digest = md.digest();
        if (logger.isDebugEnabled()) {
            logger.debug(protoName + "localized key: " + new OctetString(digest).toHexString());
        }
        return digest;
    }
