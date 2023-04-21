    InitSecContextToken(Krb5Context context, EncryptionKey[] keys, InputStream is) throws IOException, GSSException, KrbException {
        int tokenId = ((is.read() << 8) | is.read());
        if (tokenId != Krb5Token.AP_REQ_ID) throw new GSSException(GSSException.DEFECTIVE_TOKEN, -1, "AP_REQ token id does not match!");
        byte[] apReqBytes = new sun.security.util.DerValue(is).toByteArray();
        InetAddress addr = null;
        if (context.getChannelBinding() != null) {
            addr = context.getChannelBinding().getInitiatorAddress();
        }
        apReq = new KrbApReq(apReqBytes, keys, addr);
        EncryptionKey sessionKey = apReq.getCreds().getSessionKey();
        EncryptionKey subKey = apReq.getSubKey();
        if (subKey != null) {
            context.setKey(subKey);
        } else {
            context.setKey(sessionKey);
        }
        OverloadedChecksum gssChecksum = new OverloadedChecksum(context, apReq.getChecksum(), sessionKey);
        gssChecksum.setContextFlags(context);
        Credentials delegCred = gssChecksum.getDelegatedCreds();
        if (delegCred != null) {
            Krb5CredElement credElement = Krb5InitCredential.getInstance((Krb5NameElement) context.getSrcName(), delegCred);
            context.setDelegCred(credElement);
        }
        Integer apReqSeqNumber = apReq.getSeqNumber();
        int peerSeqNumber = (apReqSeqNumber != null ? apReqSeqNumber.intValue() : 0);
        context.resetPeerSequenceNumber(peerSeqNumber);
        if (!context.getMutualAuthState()) context.resetMySequenceNumber(peerSeqNumber);
        context.setAuthTime(new KerberosTime(apReq.getCreds().getAuthTime()).toString());
        context.setTktFlags(apReq.getCreds().getFlags());
        AuthorizationData ad = apReq.getCreds().getAuthzData();
        if (ad == null) {
            context.setAuthzData(null);
        } else {
            AuthorizationDataEntry[] authzData = new AuthorizationDataEntry[ad.count()];
            for (int i = 0; i < ad.count(); i++) {
                authzData[i] = new AuthorizationDataEntry(ad.item(i).adType, ad.item(i).adData);
            }
            context.setAuthzData(authzData);
        }
    }
