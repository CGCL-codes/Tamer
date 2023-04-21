    void rsaChallengeResponse(RSAPrivateKey privKey, BigInteger challenge) throws IOException {
        RSACipher rsa = new RSACipher(new KeyPair(null, privKey));
        MessageDigest md5;
        challenge = rsa.doPrivate(challenge);
        challenge = rsa.stripPad(challenge);
        byte[] response = challenge.toByteArray();
        try {
            md5 = MessageDigest.getInstance("MD5");
            if (response[0] == 0) md5.update(response, 1, 32); else md5.update(response, 0, 32);
            md5.update(sessionId);
            response = md5.digest();
        } catch (Exception e) {
            throw new IOException("MD5 not implemented, can't generate session-id");
        }
        SSHPduOutputStream outpdu = new SSHPduOutputStream(CMSG_AUTH_RSA_RESPONSE, sndCipher);
        outpdu.write(response, 0, response.length);
        outpdu.writeTo(sshOut);
        if (!isSuccess()) throw new AuthFailException("Permission denied");
    }
