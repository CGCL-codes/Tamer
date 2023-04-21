    public void registerPrefix(ContentName prefixToRegister, PublisherPublicKeyDigest publisher, Integer faceID, Integer flags, Integer lifetime) throws CCNDaemonException {
        if (null == publisher) {
            try {
                publisher = _manager.getCCNDId();
            } catch (IOException e1) {
                Log.warning(Log.FAC_NETMANAGER, "Unable to get ccnd id");
                Log.warningStackTrace(e1);
                throw new CCNDaemonException(e1.getMessage());
            }
        }
        ForwardingEntry forward = new ForwardingEntry(ActionType.Register, prefixToRegister, publisher, faceID, flags, lifetime);
        final String startURI = "ccnx:/ccnx/";
        ContentName interestName = null;
        try {
            interestName = ContentName.fromURI(startURI);
            interestName = ContentName.fromNative(interestName, _manager.getCCNDId().digest());
            interestName = ContentName.fromNative(interestName, ActionType.Register.value());
        } catch (MalformedContentNameStringException e) {
            String reason = e.getMessage();
            String msg = ("MalformedContentNameStringException in call creating ContentName for Interest, reason: " + reason);
            Log.warning(Log.FAC_NETMANAGER, msg);
            Log.warningStackTrace(e);
            throw new CCNDaemonException(msg);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CCNDaemonException(e.getMessage());
        }
        super.sendIt(interestName, forward, null, true);
    }
