    byte[] getDigestBytes(String input) {
        try {
            MessageDigest md = getMessageDigest();
            if (md == null) {
                return null;
            }
            byte[] inputBytes = input.getBytes(CHARSET);
            return md.digest(inputBytes);
        } catch (UnsupportedEncodingException x) {
            System.err.println("Unsupported encoding: " + CHARSET);
            return null;
        }
    }
