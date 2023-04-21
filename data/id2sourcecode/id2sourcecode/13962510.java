    public static void decode(ConnectionHandler p, InStream in) {
        if (p.getConnectionStage() == Constants.LOGIN_START) {
            OutStream outstream = new OutStream();
            outstream.writeByte(0);
            outstream.writeLong(p.getSessionKey());
            p.write(outstream);
            p.setConnectionStage(Constants.LOGIN_CYPTION);
        } else if (p.getConnectionStage() == Constants.LOGIN_CYPTION) {
            if (3 > in.remaining()) {
                return;
            }
            int loginType = in.readUnsignedByte();
            if (loginType != 16 && loginType != 18) {
                p.setConnectionStage(Constants.DISCONNECT);
                return;
            }
            int loginPacketSize = in.readUnsignedShort();
            if (loginPacketSize > in.remaining()) {
                return;
            }
            int clientVersion = in.readInt();
            if (clientVersion != Constants.REVISION) {
                p.setConnectionStage(Constants.DISCONNECT);
                return;
            }
            int unknown0 = in.readUnsignedByte();
            int displayMode = in.readUnsignedByte();
            p.setDisplayMode(displayMode);
            int screenSizeX = in.readUnsignedShort();
            int screenSizeY = in.readUnsignedShort();
            int unknown3 = in.readUnsignedByte();
            InStream inStream1 = new InStream(24);
            inStream1.addBytes(in.buffer(), in.offset(), 24);
            in.skip(24);
            String settings = in.readRS2String();
            int unknown4 = in.readInt();
            int size = in.readUnsignedByte();
            InStream inStream2 = new InStream(size);
            inStream2.addBytes(in.buffer(), in.offset(), size);
            in.skip(size);
            InStream inStream3 = new InStream(14);
            inStream1.addBytes(in.buffer(), in.offset(), 14);
            in.skip(14);
            in.readShort();
            in.readLong();
            int[] idxSizes = new int[33];
            for (int index = 0; index < idxSizes.length; index++) idxSizes[index] = in.readInt();
            in.skip(6);
            if (in.readUnsignedByte() != 10) {
                p.setConnectionStage(Constants.DISCONNECT);
                return;
            }
            int sessionKey[] = new int[4];
            for (int i = 0; i < 4; i++) sessionKey[i] = in.readInt();
            long l = in.readLong();
            int hash = (int) (31 & l >> 16);
            if (hash != p.getNameHash()) {
                p.setConnectionStage(Constants.DISCONNECT);
                return;
            }
            String Username = Misc.formatPlayerNameForProtocol(Misc.longToString(l));
            String Password = in.readRS2String();
            for (int i = 0; i < 4; i++) sessionKey[i] += 50;
            byte OpCode = 0;
            File account = AccountFile(Username);
            Player player = null;
            if (account == null || Username == null || Password == null) {
                OpCode = Constants.INVALID_PASSWORD;
            } else if (World.isOnList(Username)) {
                OpCode = Constants.ALREADY_ONLINE;
            } else if (!account.exists()) {
                player = new Player(Username, Password, new GregorianCalendar(), new GregorianCalendar(), (short) 1, "", (byte) 1);
                OpCode = 2;
            } else {
                player = Serializer.LoadAccount(account);
                if (!Password.equals(player.getPassword())) {
                    OpCode = Constants.INVALID_PASSWORD;
                } else if (player.isBanned()) {
                    OpCode = Constants.BANNED;
                } else {
                    OpCode = 2;
                }
            }
            long waitTime = 0;
            if (OpCode == 2) {
                String ip = "" + p.getChannel().getLocalAddress();
                ip = ip.replaceAll("/", "");
                ip = ip.replaceAll(" ", "");
                ip = ip.substring(0, ip.indexOf(":"));
                int ipInt = Misc.IPAddressToNumber(ip);
                if (World.getIps().containsKey(ipInt)) {
                    waitTime = (System.currentTimeMillis() - World.getIps().get(ipInt)) / 1000;
                    if (waitTime <= 42) {
                    }
                }
            }
            OutStream outstream = new OutStream();
            outstream.writeByte(OpCode);
            if (OpCode == 21) outstream.writeByte((int) (42 - waitTime));
            p.write(outstream);
            if (OpCode != 2) {
                p.setConnectionStage(Constants.DISCONNECT);
                return;
            }
            p.setConnectionStage(Constants.REMOVE_ID);
            p.setPlayer(player);
            World.registerConnection(p);
        } else {
            p.setConnectionStage(Constants.DISCONNECT);
        }
    }
