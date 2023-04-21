    public boolean verify() throws SignatureException {
        if (verified) return verifyResult;
        if (isTsp) {
            TimeStampTokenInfo info = timeStampToken.getTimeStampInfo();
            MessageImprint imprint = info.toASN1Structure().getMessageImprint();
            byte[] md = messageDigest.digest();
            byte[] imphashed = imprint.getHashedMessage();
            verifyResult = Arrays.equals(md, imphashed);
        } else {
            if (sigAttr != null) {
                final byte[] msgDigestBytes = messageDigest.digest();
                boolean verifyRSAdata = true;
                sig.update(sigAttr);
                boolean encContDigestCompare = false;
                if (RSAdata != null) {
                    verifyRSAdata = Arrays.equals(msgDigestBytes, RSAdata);
                    encContDigest.update(RSAdata);
                    encContDigestCompare = Arrays.equals(encContDigest.digest(), digestAttr);
                }
                boolean absentEncContDigestCompare = Arrays.equals(msgDigestBytes, digestAttr);
                boolean concludingDigestCompare = absentEncContDigestCompare || encContDigestCompare;
                boolean sigVerify = sig.verify(digest);
                verifyResult = concludingDigestCompare && sigVerify && verifyRSAdata;
            } else {
                if (RSAdata != null) sig.update(messageDigest.digest());
                verifyResult = sig.verify(digest);
            }
        }
        verified = true;
        return verifyResult;
    }
