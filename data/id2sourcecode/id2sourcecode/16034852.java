    private static boolean handle_digest(AuthorizationInfo prev, Response resp, RoRequest req, String hdr_name) throws IOException {
        if (prev == null) return false;
        NVPair[] params = prev.getParams();
        VerifyDigest verifier = new VerifyDigest(((String[]) prev.getExtraInfo())[0], getValue(params, "nonce"), req.getMethod(), getValue(params, "uri"), hdr_name, resp);
        if (resp.hasEntity()) {
            Log.write(Log.AUTH, "Auth:  pushing md5-check-stream to verify " + "digest from " + hdr_name);
            resp.inp_stream = new MD5InputStream(resp.inp_stream, verifier);
        } else {
            Log.write(Log.AUTH, "Auth:  verifying digest from " + hdr_name);
            verifier.verifyHash(MD5.digest(NUL), 0);
        }
        return true;
    }
