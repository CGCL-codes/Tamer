    public boolean apop(String username, String password) throws IOException {
        if (username == null || password == null || timestamp == null) {
            return false;
        }
        try {
            byte[] secret = password.getBytes("US-ASCII");
            byte[] target = new byte[timestamp.length + secret.length];
            System.arraycopy(timestamp, 0, target, 0, timestamp.length);
            System.arraycopy(secret, 0, target, timestamp.length, secret.length);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] db = md5.digest(target);
            StringBuffer digest = new StringBuffer();
            for (int i = 0; i < db.length; i++) {
                int c = (int) db[i];
                if (c < 0) {
                    c += 256;
                }
                digest.append(Integer.toHexString((c & 0xf0) >> 4));
                digest.append(Integer.toHexString(c & 0x0f));
            }
            String cmd = new StringBuffer(APOP).append(' ').append(username).append(' ').append(digest.toString()).toString();
            send(cmd);
            return getResponse() == OK;
        } catch (NoSuchAlgorithmException e) {
            logger.log(POP3_TRACE, "MD5 algorithm not found");
            return false;
        }
    }
