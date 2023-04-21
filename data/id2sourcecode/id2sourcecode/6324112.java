    protected int engineSign(byte[] outbuf, int offset, int len) throws SignatureException {
        byte[] signature = sign(digest.digest());
        if (signature.length < len) {
            len = signature.length;
        }
        System.arraycopy(signature, 0, outbuf, offset, len);
        return len;
    }
