    public ForwardingEntry selfRegisterPrefix(ContentName prefixToRegister, Integer faceID, Integer flags, Integer lifetime) throws CCNDaemonException {
        final String startURI = "ccnx:/ccnx/";
        PublisherPublicKeyDigest ccndId;
        try {
            ccndId = _manager.getCCNDId();
        } catch (IOException e1) {
            e1.printStackTrace();
            throw new CCNDaemonException(e1.getMessage());
        }
        ContentName interestName;
        try {
            interestName = ContentName.fromURI(startURI);
            interestName = ContentName.fromNative(interestName, ccndId.digest());
            interestName = ContentName.fromNative(interestName, ActionType.SelfRegister.value());
        } catch (MalformedContentNameStringException e) {
            String reason = e.getMessage();
            String msg = ("Unexpected MalformedContentNameStringException in call creating ContentName " + startURI + ", reason: " + reason);
            Log.warning(Log.FAC_NETMANAGER, msg);
            Log.warningStackTrace(e);
            throw new CCNDaemonException(msg);
        }
        ForwardingEntry forward = new ForwardingEntry(ActionType.SelfRegister, prefixToRegister, ccndId, faceID, flags, lifetime);
        byte[] payloadBack = super.sendIt(interestName, forward, null, true);
        ForwardingEntry entryBack = new ForwardingEntry(payloadBack);
        Log.fine(Log.FAC_NETMANAGER, "registerPrefix: returned {0}", entryBack);
        return entryBack;
    }
