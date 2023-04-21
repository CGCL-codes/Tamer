    public void Write(ChannelWriter chan) throws IOException {
        chan.putByte(HEADERBYTE);
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
        if (R == null || S == null) {
            chan.putByte((byte) 0);
        } else {
            chan.putByte((byte) 1);
            BigIntChannel bi = new BigIntChannel();
            bi.BI = R;
            chan.Write(bi);
            bi.BI = S;
            chan.Write(bi);
        }
    }
