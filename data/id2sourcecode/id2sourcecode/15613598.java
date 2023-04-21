    private void calcSignature(byte[] bytes) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        md.update(bytes, 32, bytes.length - 32);
        try {
            int amt = md.digest(bytes, 12, 20);
            if (amt != 20) {
                throw new RuntimeException("unexpected digest write: " + amt + " bytes");
            }
        } catch (DigestException ex) {
            throw new RuntimeException(ex);
        }
    }
