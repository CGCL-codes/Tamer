    private FaceInstance sendIt(FaceInstance face) throws CCNDaemonException {
        final String startURI = "ccnx:/ccnx/";
        ContentName interestName = null;
        try {
            interestName = ContentName.fromURI(startURI);
            interestName = ContentName.fromNative(interestName, getId().digest());
            interestName = ContentName.fromNative(interestName, face.action());
        } catch (MalformedContentNameStringException e) {
            Log.fine("Call to create ContentName failed: " + e.getMessage() + "\n");
            String msg = ("Unexpected MalformedContentNameStringException in call creating ContentName, reason: " + e.getMessage());
            throw new CCNDaemonException(msg);
        }
        byte[] payloadBack = super.sendIt(interestName, face, null, true);
        FaceInstance faceBack = new FaceInstance(payloadBack);
        String formattedFace = faceBack.toFormattedString();
        Log.fine(formattedFace);
        return faceBack;
    }
