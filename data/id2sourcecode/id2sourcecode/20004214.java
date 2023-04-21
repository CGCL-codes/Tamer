    public int checkNonce(String nonce, HttpRequest request) {
        try {
            byte[] n = B64Code.decode(nonce.toCharArray());
            if (n.length != 24) return -1;
            long ts = 0;
            long sk = nonceSecret;
            byte[] n2 = new byte[16];
            for (int i = 0; i < 8; i++) {
                n2[i] = n[i];
                n2[8 + i] = (byte) (sk & 0xff);
                sk = sk >> 8;
                ts = (ts << 8) + (0xff & (long) n[7 - i]);
            }
            long age = request.getTimeStamp() - ts;
            if (log.isDebugEnabled()) log.debug("age=" + age);
            byte[] hash = null;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.reset();
                md.update(n2, 0, 16);
                hash = md.digest();
            } catch (Exception e) {
                log.fatal(this, e);
            }
            for (int i = 0; i < 16; i++) if (n[i + 8] != hash[i]) return -1;
            if (maxNonceAge > 0 && (age < 0 || age > maxNonceAge)) return 0;
            return 1;
        } catch (Exception e) {
            log.debug("", e);
        }
        return -1;
    }
