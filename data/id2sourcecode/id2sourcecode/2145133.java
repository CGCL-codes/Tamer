    public String generateResponse() {
        if (userName == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: no userName parameter");
            return null;
        }
        if (realm == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: no realm parameter");
            return null;
        }
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "Trying to generate a response for the user: " + userName + " , with " + "the realm: " + realm);
        if (password == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: no password parameter");
            return null;
        }
        if (method == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: no method parameter");
            return null;
        }
        if (uri == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: no uri parameter");
            return null;
        }
        if (nonce == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: no nonce parameter");
            return null;
        }
        if (messageDigest == null) {
            System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(): " + "ERROR: the algorithm is not set");
            return null;
        }
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), userName:" + userName + "!");
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), realm:" + realm + "!");
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), password:" + password + "!");
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), uri:" + uri + "!");
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), nonce:" + nonce + "!");
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), method:" + method + "!");
        String A1 = userName + ":" + realm + ":" + password;
        String A2 = method.toUpperCase() + ":" + uri;
        byte mdbytes[] = messageDigest.digest(A1.getBytes());
        String HA1 = toHexString(mdbytes);
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), HA1:" + HA1 + "!");
        mdbytes = messageDigest.digest(A2.getBytes());
        String HA2 = toHexString(mdbytes);
        System.out.println("DEBUG, DigestClientAuthenticationMethod, generateResponse(), HA2:" + HA2 + "!");
        String KD = HA1 + ":" + nonce;
        if (cnonce != null) {
            KD += ":" + cnonce;
        }
        KD += ":" + HA2;
        mdbytes = messageDigest.digest(KD.getBytes());
        String response = toHexString(mdbytes);
        System.out.println("DEBUG, DigestClientAlgorithm, generateResponse():" + " response generated: " + response);
        return response;
    }
