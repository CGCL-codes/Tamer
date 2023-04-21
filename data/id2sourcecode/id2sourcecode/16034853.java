    private static boolean handle_rspauth(AuthorizationInfo prev, Response resp, RoRequest req, Vector auth_info, String hdr_name) throws IOException {
        if (prev == null) return false;
        NVPair[] params = prev.getParams();
        int uri = -1, alg = -1, nonce = -1, cnonce = -1, nc = -1;
        for (int idx = 0; idx < params.length; idx++) {
            String name = params[idx].getName().toLowerCase();
            if (name.equals("uri")) uri = idx; else if (name.equals("algorithm")) alg = idx; else if (name.equals("nonce")) nonce = idx; else if (name.equals("cnonce")) cnonce = idx; else if (name.equals("nc")) nc = idx;
        }
        VerifyRspAuth verifier = new VerifyRspAuth(params[uri].getValue(), ((String[]) prev.getExtraInfo())[0], (alg == -1 ? null : params[alg].getValue()), params[nonce].getValue(), (cnonce == -1 ? "" : params[cnonce].getValue()), (nc == -1 ? "" : params[nc].getValue()), hdr_name, resp);
        HttpHeaderElement qop = null;
        if (auth_info != null && (qop = Util.getElement(auth_info, "qop")) != null && qop.getValue() != null && (qop.getValue().equalsIgnoreCase("auth") || !resp.hasEntity() && qop.getValue().equalsIgnoreCase("auth-int"))) {
            Log.write(Log.AUTH, "Auth:  verifying rspauth from " + hdr_name);
            verifier.verifyHash(MD5.digest(NUL), 0);
        } else {
            Log.write(Log.AUTH, "Auth:  pushing md5-check-stream to verify " + "rspauth from " + hdr_name);
            resp.inp_stream = new MD5InputStream(resp.inp_stream, verifier);
        }
        return true;
    }
