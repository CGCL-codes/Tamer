    protected boolean matches(final String undigested, final String reference) {
        final String compare;
        if (digest != null) {
            compare = digest.digest(undigested.getBytes(), converter);
        } else {
            compare = undigested;
        }
        return reference.equals(compare);
    }
