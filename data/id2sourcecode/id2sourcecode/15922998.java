    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splittedLine) throws Exception, IllegalCommandSyntaxException {
        try {
            Map<Integer, Integer> connected = c.getChannelServer().getWorldInterface().getConnected();
            StringBuilder conStr = new StringBuilder("Connected Clients: ");
            boolean first = true;
            for (int i : connected.keySet()) {
                if (!first) {
                    conStr.append(", ");
                } else {
                    first = false;
                }
                if (i == 0) {
                    conStr.append("Total: ");
                    conStr.append(connected.get(i));
                } else {
                    conStr.append("Channel");
                    conStr.append(i);
                    conStr.append(": ");
                    conStr.append(connected.get(i));
                }
            }
            new ServernoticeMapleClientMessageCallback(c).dropMessage(conStr.toString());
        } catch (RemoteException e) {
            c.getChannelServer().reconnectWorld();
        }
    }
