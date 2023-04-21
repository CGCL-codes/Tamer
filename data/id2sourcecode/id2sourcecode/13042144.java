    public static void run(ConnectionHandler p, InStream in) {
        while (in.remaining() > 0) {
            byte Stage = p.getConnectionStage();
            if (Stage == Constants.DISCONNECT || !p.getChannel().isConnected()) {
                p.getChannel().close();
                break;
            } else if (Stage == Constants.GET_CONNECTION_ID) {
                int conid = in.readUnsignedByte();
                if (conid == 14) {
                    p.setNameHash((byte) in.readUnsignedByte());
                    long serverSessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D);
                    p.setSessionKey(serverSessionKey);
                    p.setConnectionStage((byte) Constants.LOGIN_START);
                    LoginDecoder.decode(p, in);
                } else if (conid == 15) {
                    p.setConnectionStage(Constants.UPDATESERVER_PART1);
                    UpdateServerDecoder.decode(p, in);
                } else if (conid == 20) {
                    p.setConnectionStage(Constants.CHECK_ACC_COUNTRY);
                    AccountCreationDecoder.decode(p, in);
                } else if (conid == 21) {
                    p.setConnectionStage(Constants.CHECK_ACC_NAME);
                    AccountCreationDecoder.decode(p, in);
                } else if (conid == 22) {
                    p.setConnectionStage(Constants.MAKE_ACC);
                    AccountCreationDecoder.decode(p, in);
                } else {
                    int PacketSize = in.readUnsignedByte();
                }
            } else if (Stage == Constants.UPDATESERVER_PART2) {
                UpdateServerDecoder.decode(p, in);
            } else if (Stage == Constants.CHECK_ACC_NAME || Stage == Constants.CHECK_ACC_COUNTRY || Stage == Constants.MAKE_ACC) {
                AccountCreationDecoder.decode(p, in);
            } else if (Stage == Constants.LOGIN_START || Stage == Constants.LOGIN_CYPTION) {
                LoginDecoder.decode(p, in);
            } else if (Stage == Constants.REMOVE_ID) {
                break;
            } else {
                p.setConnectionStage(Constants.DISCONNECT);
            }
        }
    }
