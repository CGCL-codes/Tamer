    public boolean Verify(DSA dsa, CipherParameters pubkey) {
        WriteBytesChannel bc = new WriteBytesChannel();
        ChannelWriter chan = new ChannelWriter(bc);
        try {
            chan.putLong(MintID);
            chan.putLong(RequestNumber);
            chan.putInt(SigningRank);
            chan.putLong(MintNodeID);
            if (NodePublicKey != null) {
                chan.putByte((byte) 1);
                chan.Write(NodePublicKey.getChannel());
            } else {
                chan.putByte((byte) 0);
            }
            chan.putString(IssuingServerConnectString);
            chan.putLong(IssuingServerID);
            chan.putLong(ExpirationTime);
            chan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dsa.init(false, pubkey);
        return dsa.verifySignature(bc.getBytes(), R, S);
    }
