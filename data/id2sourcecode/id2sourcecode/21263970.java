    public long getChannel() throws XBeeOperationFailedException {
        int frameID;
        frameID = sendATCommand(new ATCommandPayloadFactory().queryCH());
        ATCommandResponse.CH resp1 = listener.getResponse(frameID);
        return resp1.getValue();
    }
