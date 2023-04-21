    private int rcvSrvrKeyExch() throws IOException {
        int msgLength = getNextMsg(S_KEYEXCH);
        int idx = start + HDR_SIZE;
        int endOfMsg = start + msgLength;
        RSAPublicKey sKey = (RSAPublicKey) sCert.getPublicKey();
        int keyUsage = sCert.getKeyUsage();
        if (msgLength == -1) {
            eKey = sKey;
            if (keyUsage != -1 && (keyUsage & X509Certificate.KEY_ENCIPHER_KEY_USAGE) != X509Certificate.KEY_ENCIPHER_KEY_USAGE) {
                if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                    Logging.report(Logging.ERROR, LogChannels.LC_SECURITY, "The keyEncipherment was bit is " + "set in server certificate key " + "usage extension.");
                }
                throw new CertificateException(sCert, CertificateException.INAPPROPRIATE_KEY_USAGE);
            }
            return 0;
        }
        if (endOfMsg < (idx + 4)) {
            return -1;
        }
        int len = ((rec.inputData[idx++] & 0xff) << 16) + (rec.inputData[idx++] & 0xff);
        if (endOfMsg < (idx + len + 2)) {
            return -1;
        }
        int modulusPos;
        int modulusLen;
        int exponentPos;
        int exponentLen;
        if ((len == 65) && (rec.inputData[idx] == (byte) 0x00)) {
            modulusPos = idx + 1;
            modulusLen = 64;
        } else {
            modulusPos = idx;
            modulusLen = len;
        }
        idx += len;
        len = ((rec.inputData[idx++] & 0xff) << 16) + (rec.inputData[idx++] & 0xff);
        if (endOfMsg < (idx + len)) {
            return -1;
        }
        exponentPos = idx;
        exponentLen = len;
        eKey = new RSAPublicKey(rec.inputData, modulusPos, modulusLen, rec.inputData, exponentPos, exponentLen);
        idx += len;
        int end = idx;
        len = ((rec.inputData[idx++] & 0xff) << 16) + (rec.inputData[idx++] & 0xff);
        if (endOfMsg < (idx + len)) {
            return -1;
        }
        byte[] sig = new byte[len];
        System.arraycopy(rec.inputData, idx, sig, 0, sig.length);
        idx += len;
        if (endOfMsg != idx) {
            return -1;
        }
        byte[] dat = new byte[MD5_SIZE + SHA_SIZE];
        try {
            MessageDigest di = MessageDigest.getInstance("MD5");
            di.update(crand, 0, crand.length);
            di.update(srand, 0, srand.length);
            di.update(rec.inputData, HDR_SIZE, end - HDR_SIZE);
            di.digest(dat, 0, MD5_SIZE);
            di = MessageDigest.getInstance("SHA-1");
            di.update(crand, 0, crand.length);
            di.update(srand, 0, srand.length);
            di.update(rec.inputData, HDR_SIZE, end - HDR_SIZE);
            di.digest(dat, MD5_SIZE, SHA_SIZE);
        } catch (Exception e) {
            throw new RuntimeException("No MD5 or SHA");
        }
        try {
            Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, sKey);
            byte[] res = new byte[sKey.getModulusLen()];
            int val = rsa.doFinal(sig, 0, sig.length, res, 0);
            if (!Utils.byteMatch(res, 0, dat, 0, dat.length)) {
                if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                    Logging.report(Logging.ERROR, LogChannels.LC_SECURITY, "RSA params failed verification");
                }
                return -1;
            }
        } catch (Exception e) {
            throw new IOException("RSA decryption caught " + e);
        }
        ourMD5.update(rec.inputData, start, msgLength);
        ourSHA.update(rec.inputData, start, msgLength);
        return 0;
    }
