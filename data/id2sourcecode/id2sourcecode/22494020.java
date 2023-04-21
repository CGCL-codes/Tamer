    private byte[] getDigest() throws SignatureException {
        try {
            initDigests();
            byte[] data = new byte[36];
            md5.digest(data, 0, 16);
            sha.digest(data, 16, 20);
            isReset = true;
            return data;
        } catch (DigestException e) {
            throw new SignatureException(e);
        }
    }
