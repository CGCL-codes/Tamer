    public void verifyHash(byte[] hash, long len) throws IOException {
        String auth_info = resp.getHeader(hdr);
        if (auth_info == null) auth_info = resp.getTrailer(hdr);
        if (auth_info == null) return;
        Vector pai;
        try {
            pai = Util.parseHeader(auth_info);
        } catch (ParseException pe) {
            throw new IOException(pe.toString());
        }
        String qop;
        HttpHeaderElement elem = Util.getElement(pai, "qop");
        if (elem == null || (qop = elem.getValue()) == null || (!qop.equalsIgnoreCase("auth") && !qop.equalsIgnoreCase("auth-int"))) return;
        elem = Util.getElement(pai, "rspauth");
        if (elem == null || elem.getValue() == null) return;
        byte[] digest = DefaultAuthHandler.unHex(elem.getValue());
        elem = Util.getElement(pai, "cnonce");
        if (elem != null && elem.getValue() != null && !elem.getValue().equals(cnonce)) throw new IOException("Digest auth scheme: received wrong " + "client-nonce '" + elem.getValue() + "' - expected '" + cnonce + "'");
        elem = Util.getElement(pai, "nc");
        if (elem != null && elem.getValue() != null && !elem.getValue().equals(nc)) throw new IOException("Digest auth scheme: received wrong " + "nonce-count '" + elem.getValue() + "' - expected '" + nc + "'");
        String A1, A2;
        if (alg != null && alg.equalsIgnoreCase("MD5-sess")) A1 = MD5.hexDigest(HA1 + ":" + nonce + ":" + cnonce); else A1 = HA1;
        A2 = ":" + uri;
        if (qop.equalsIgnoreCase("auth-int")) A2 += ":" + MD5.toHex(hash);
        A2 = MD5.hexDigest(A2);
        hash = MD5.digest(A1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + A2);
        for (int idx = 0; idx < hash.length; idx++) {
            if (hash[idx] != digest[idx]) throw new IOException("MD5-Digest mismatch: expected " + DefaultAuthHandler.hex(digest) + " but calculated " + DefaultAuthHandler.hex(hash));
        }
        Log.write(Log.AUTH, "Auth:  rspauth from " + hdr + " successfully verified");
    }
