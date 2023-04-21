    public void Sign(DSA dsa, CipherParameters privkey) {
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
        dsa.init(true, privkey);
        BigInteger[] b = dsa.generateSignature(bc.getBytes());
        R = b[0];
        S = b[1];
    }
