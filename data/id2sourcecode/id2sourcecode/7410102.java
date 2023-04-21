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
        HttpHeaderElement elem = Util.getElement(pai, "digest");
        if (elem == null || elem.getValue() == null) return;
        byte[] digest = DefaultAuthHandler.unHex(elem.getValue());
        String entity_info = MD5.hexDigest(uri + ":" + header_val("Content-Type", resp) + ":" + header_val("Content-Length", resp) + ":" + header_val("Content-Encoding", resp) + ":" + header_val("Last-Modified", resp) + ":" + header_val("Expires", resp));
        hash = MD5.digest(HA1 + ":" + nonce + ":" + method + ":" + header_val("Date", resp) + ":" + entity_info + ":" + MD5.toHex(hash));
        for (int idx = 0; idx < hash.length; idx++) {
            if (hash[idx] != digest[idx]) throw new IOException("MD5-Digest mismatch: expected " + DefaultAuthHandler.hex(digest) + " but calculated " + DefaultAuthHandler.hex(hash));
        }
        Log.write(Log.AUTH, "Auth:  digest from " + hdr + " successfully verified");
    }
