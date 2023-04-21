    void generateSessionId() throws IOException {
        byte[] message;
        byte[] srvKey = srvServerKey.getModulus().toByteArray();
        byte[] hstKey = srvHostKey.getModulus().toByteArray();
        int len = srvKey.length + hstKey.length + srvCookie.length;
        if (srvKey[0] == 0) len -= 1;
        if (hstKey[0] == 0) len -= 1;
        message = new byte[len];
        if (hstKey[0] == 0) {
            System.arraycopy(hstKey, 1, message, 0, hstKey.length - 1);
            len = hstKey.length - 1;
        } else {
            System.arraycopy(hstKey, 0, message, 0, hstKey.length);
            len = hstKey.length;
        }
        if (srvKey[0] == 0) {
            System.arraycopy(srvKey, 1, message, len, srvKey.length - 1);
            len += srvKey.length - 1;
        } else {
            System.arraycopy(srvKey, 0, message, len, srvKey.length);
            len += srvKey.length;
        }
        System.arraycopy(srvCookie, 0, message, len, srvCookie.length);
        try {
            MessageDigest md5;
            md5 = MessageDigest.getInstance("MD5");
            md5.update(message);
            sessionId = md5.digest();
        } catch (Exception e) {
            throw new IOException("MD5 not implemented, can't generate session-id");
        }
    }
