    void getUserSessionKey(byte[] challenge, byte[] dest, int offset) throws Exception {
        if (hashesExternal) return;
        MD4 md4 = new MD4();
        md4.update(password.getBytes("UnicodeLittleUnmarked"));
        switch(LM_COMPATIBILITY) {
            case 0:
            case 1:
            case 2:
                md4.update(md4.digest());
                md4.digest(dest, offset, 16);
                break;
            case 3:
            case 4:
            case 5:
                if (clientChallenge == null) {
                    clientChallenge = new byte[8];
                    RANDOM.nextBytes(clientChallenge);
                }
                HMACT64 hmac = new HMACT64(md4.digest());
                hmac.update(username.toUpperCase().getBytes("UnicodeLittleUnmarked"));
                hmac.update(domain.toUpperCase().getBytes("UnicodeLittleUnmarked"));
                byte[] ntlmv2Hash = hmac.digest();
                hmac = new HMACT64(ntlmv2Hash);
                hmac.update(challenge);
                hmac.update(clientChallenge);
                HMACT64 userKey = new HMACT64(ntlmv2Hash);
                userKey.update(hmac.digest());
                userKey.digest(dest, offset, 16);
                break;
            default:
                md4.update(md4.digest());
                md4.digest(dest, offset, 16);
                break;
        }
    }
