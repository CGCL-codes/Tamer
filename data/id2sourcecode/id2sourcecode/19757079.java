    String checkMsgDigest(byte[] msgDigest, byte[] msg) {
        try {
            MessageDigest digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM, getProviderByAlgorithm(MESSAGE_DIGEST_ALGORITHM));
            byte[] result = digest.digest(msg);
            boolean ok = MessageDigest.isEqual(result, msgDigest);
            if (ok) {
                if (super.isVerbose()) {
                    super.fireOffWarningInformation("MqJmsJceBaseBean:checkMsgDigest() - Message Digest Verification Passed");
                }
                return "true";
            } else {
                return "MqJmsJceBaseBean:checkMsgDigest() - Message Digest Verification Failed";
            }
        } catch (GeneralSecurityException e) {
            String errMsg$ = "MqJmsJceBaseBean:checkMsgDigest() - " + e.getMessage();
            super.fireOffWarningInformation(errMsg$);
            return errMsg$;
        }
    }
