    public boolean verify() throws SignatureException {
        if (verified) return verifyResult;
        if (sigAttr != null) {
            sig.update(sigAttr);
            if (RSAdata != null) {
                byte msd[] = messageDigest.digest();
                messageDigest.update(msd);
            }
            verifyResult = (Arrays.equals(messageDigest.digest(), digestAttr) && sig.verify(digest));
        } else {
            if (RSAdata != null) sig.update(messageDigest.digest());
            verifyResult = sig.verify(digest);
        }
        verified = true;
        return verifyResult;
    }
