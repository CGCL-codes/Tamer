    public static byte[] getLMv2Response(String domain, String user, String password, byte[] challenge, byte[] clientChallenge) {
        try {
            byte[] hash = new byte[16];
            byte[] response = new byte[24];
            MD4 md4 = new MD4();
            md4.update(password.getBytes("UnicodeLittleUnmarked"));
            HMACT64 hmac = new HMACT64(md4.digest());
            hmac.update(user.toUpperCase().getBytes("UnicodeLittleUnmarked"));
            hmac.update(domain.toUpperCase().getBytes("UnicodeLittleUnmarked"));
            hmac = new HMACT64(hmac.digest());
            hmac.update(challenge);
            hmac.update(clientChallenge);
            hmac.digest(response, 0, 16);
            System.arraycopy(clientChallenge, 0, response, 16, 8);
            return response;
        } catch (Exception ex) {
            if (log.level > 0) ex.printStackTrace(log);
            return null;
        }
    }
