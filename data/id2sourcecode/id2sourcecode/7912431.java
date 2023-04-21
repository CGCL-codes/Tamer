    public void unRegisterPrefix(ContentName prefixName, RegisteredPrefix prefix, Integer faceID) throws CCNDaemonException {
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
            interestName = ContentName.fromNative(interestName, ActionType.UnRegister.value());
        } catch (MalformedContentNameStringException e) {
            String reason = e.getMessage();
            String msg = ("Unexpected MalformedContentNameStringException in call creating ContentName " + startURI + ", reason: " + reason);
            Log.warning(Log.FAC_NETMANAGER, msg);
            Log.warningStackTrace(e);
            throw new CCNDaemonException(msg);
        }
        ForwardingEntry forward = new ForwardingEntry(ActionType.UnRegister, prefixName, ccndId, faceID, null, null);
        super.sendIt(interestName, forward, prefix, false);
    }
