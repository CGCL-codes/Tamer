    public static void handleAuthenticationChallenge(OBEXHeaderSetImpl incomingHeaders, OBEXHeaderSetImpl replyHeaders, Authenticator authenticator) throws IOException {
        for (Enumeration iter = incomingHeaders.getAuthenticationChallenges(); iter.hasMoreElements(); ) {
            byte[] authChallenge = (byte[]) iter.nextElement();
            Challenge challenge = new Challenge(authChallenge);
            PasswordAuthentication pwd = authenticator.onAuthenticationChallenge(challenge.getRealm(), challenge.isUserIdRequired(), challenge.isFullAccess());
            DigestResponse dr = new DigestResponse();
            dr.nonce = challenge.nonce;
            if (challenge.isUserIdRequired()) {
                dr.userName = pwd.getUserName();
            }
            MD5DigestWrapper md5 = new MD5DigestWrapper();
            md5.update(dr.nonce);
            md5.update(column);
            md5.update(pwd.getPassword());
            dr.requestDigest = md5.digest();
            replyHeaders.addAuthenticationResponse(dr.write());
        }
    }
