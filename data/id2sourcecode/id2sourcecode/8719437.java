    protected synchronized String generateSessionId() {
        byte bytes[] = new byte[SESSION_ID_BYTES];
        getRandom().nextBytes(bytes);
        bytes = getDigest().digest(bytes);
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
            byte b2 = (byte) (bytes[i] & 0x0f);
            if (b1 < 10) result.append((char) ('0' + b1)); else result.append((char) ('A' + (b1 - 10)));
            if (b2 < 10) result.append((char) ('0' + b2)); else result.append((char) ('A' + (b2 - 10)));
        }
        return (result.toString());
    }
