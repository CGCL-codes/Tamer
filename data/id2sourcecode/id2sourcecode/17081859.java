    static void handleAuthenticationResponse(OBEXHeaderSetImpl incomingHeaders, Authenticator authenticator, ServerRequestHandler serverHandler) throws IOException {
        for (Enumeration iter = incomingHeaders.getAuthenticationResponses(); iter.hasMoreElements(); ) {
            byte[] authResponse = (byte[]) iter.nextElement();
            DigestResponse dr = new DigestResponse();
            dr.read(authResponse);
            byte[] password = authenticator.onAuthenticationResponse(dr.userName);
            if (password == null) {
                throw new IOException("authentication request failed");
            }
            MD5DigestWrapper md5 = new MD5DigestWrapper();
            md5.update(dr.nonce);
            md5.update(column);
            md5.update(password);
            if (!equals(dr.requestDigest, md5.digest())) {
                if (serverHandler != null) {
                    serverHandler.onAuthenticationFailure(dr.userName);
                } else {
                    throw new IOException("Authentication failure");
                }
            }
        }
    }
