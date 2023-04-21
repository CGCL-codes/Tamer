    byte[] calculateChecksum(int alg, byte[] header, byte[] trailer, byte[] data, int start, int len, int tokenId) throws GSSException {
        switch(alg) {
            case MessageToken.SGN_ALG_DES_MAC_MD5:
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(header);
                    md5.update(data, start, len);
                    if (trailer != null) {
                        md5.update(trailer);
                    }
                    data = md5.digest();
                    start = 0;
                    len = data.length;
                    header = null;
                    trailer = null;
                } catch (NoSuchAlgorithmException e) {
                    GSSException ge = new GSSException(GSSException.FAILURE, -1, "Could not get MD5 Message Digest - " + e.getMessage());
                    ge.initCause(e);
                    throw ge;
                }
            case MessageToken.SGN_ALG_DES_MAC:
                return getDesCbcChecksum(keybytes, header, data, start, len);
            case MessageToken.SGN_ALG_HMAC_SHA1_DES3_KD:
                byte[] buf;
                int offset, total;
                if (header == null && trailer == null) {
                    buf = data;
                    total = len;
                    offset = start;
                } else {
                    total = ((header != null ? header.length : 0) + len + (trailer != null ? trailer.length : 0));
                    buf = new byte[total];
                    int pos = 0;
                    if (header != null) {
                        System.arraycopy(header, 0, buf, 0, header.length);
                        pos = header.length;
                    }
                    System.arraycopy(data, start, buf, pos, len);
                    pos += len;
                    if (trailer != null) {
                        System.arraycopy(trailer, 0, buf, pos, trailer.length);
                    }
                    offset = 0;
                }
                try {
                    byte[] answer = Des3.calculateChecksum(keybytes, KG_USAGE_SIGN, buf, offset, total);
                    return answer;
                } catch (GeneralSecurityException e) {
                    GSSException ge = new GSSException(GSSException.FAILURE, -1, "Could not use HMAC-SHA1-DES3-KD signing algorithm - " + e.getMessage());
                    ge.initCause(e);
                    throw ge;
                }
            case MessageToken.SGN_ALG_HMAC_MD5_ARCFOUR:
                byte[] buffer;
                int off, tot;
                if (header == null && trailer == null) {
                    buffer = data;
                    tot = len;
                    off = start;
                } else {
                    tot = ((header != null ? header.length : 0) + len + (trailer != null ? trailer.length : 0));
                    buffer = new byte[tot];
                    int pos = 0;
                    if (header != null) {
                        System.arraycopy(header, 0, buffer, 0, header.length);
                        pos = header.length;
                    }
                    System.arraycopy(data, start, buffer, pos, len);
                    pos += len;
                    if (trailer != null) {
                        System.arraycopy(trailer, 0, buffer, pos, trailer.length);
                    }
                    off = 0;
                }
                try {
                    int key_usage = KG_USAGE_SIGN;
                    if (tokenId == Krb5Token.MIC_ID) {
                        key_usage = KG_USAGE_SIGN_MS;
                    }
                    byte[] answer = ArcFourHmac.calculateChecksum(keybytes, key_usage, buffer, off, tot);
                    byte[] output = new byte[getChecksumLength()];
                    System.arraycopy(answer, 0, output, 0, output.length);
                    return output;
                } catch (GeneralSecurityException e) {
                    GSSException ge = new GSSException(GSSException.FAILURE, -1, "Could not use HMAC_MD5_ARCFOUR signing algorithm - " + e.getMessage());
                    ge.initCause(e);
                    throw ge;
                }
            default:
                throw new GSSException(GSSException.FAILURE, -1, "Unsupported signing algorithm: " + sgnAlg);
        }
    }
