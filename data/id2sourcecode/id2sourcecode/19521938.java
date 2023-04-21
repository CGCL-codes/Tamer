    public String makeSHA(final String s) {
        final StringBuffer encoded = new StringBuffer();
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] digest = md.digest(s.getBytes());
            for (final byte d : digest) {
                encoded.append(Integer.toHexString(d & 0xFF));
            }
        } catch (final Exception e) {
            LOG.error("makeSHA: " + e.toString());
        }
        return encoded.toString();
    }
