    protected String digest(String credentials) {
        if (hasMessageDigest() == false) return (credentials);
        synchronized (this) {
            try {
                md.reset();
                byte[] bytes = null;
                if (getDigestEncoding() == null) {
                    bytes = credentials.getBytes();
                } else {
                    try {
                        bytes = credentials.getBytes(getDigestEncoding());
                    } catch (UnsupportedEncodingException uee) {
                        log.error("Illegal digestEncoding: " + getDigestEncoding(), uee);
                        throw new IllegalArgumentException(uee.getMessage());
                    }
                }
                md.update(bytes);
                return (HexUtils.convert(md.digest()));
            } catch (Exception e) {
                log.error(sm.getString("realmBase.digest"), e);
                return (credentials);
            }
        }
    }
