    public void run() {
        try {
            int possibleLogins = LoginServer.getInstance().getPossibleLogins();
            LoginServer.getInstance().getWorldInterface().isAvailable();
            if (possibleLoginHistory.size() >= (5 * 60 * 1000) / LoginServer.getInstance().getLoginInterval()) {
                possibleLoginHistory.remove(0);
            }
            possibleLoginHistory.add(possibleLogins);
            if (possibleLogins == 0 && waiting.peek().isGm()) possibleLogins = 1;
            for (int i = 0; i < possibleLogins; i++) {
                final MapleClient client;
                synchronized (waiting) {
                    if (waiting.isEmpty()) {
                        break;
                    }
                    client = waiting.removeFirst();
                }
                waitingNames.remove(client.getAccountName().toLowerCase());
                if (client.finishLogin(true) == 0) {
                    if (!client.isGuest()) {
                        client.getSession().write(MaplePacketCreator.getAuthSuccessRequestPin(client.getAccountName()));
                        client.setIdleTask(TimerManager.getInstance().schedule(new Runnable() {

                            public void run() {
                                client.getSession().close();
                            }
                        }, 10 * 60 * 10000));
                    }
                } else {
                    client.getSession().write(MaplePacketCreator.getLoginFailed(7));
                }
            }
            Map<Integer, Integer> load = LoginServer.getInstance().getWorldInterface().getChannelLoad();
            double loadFactor = 1200 / ((double) LoginServer.getInstance().getUserLimit() / load.size());
            for (Entry<Integer, Integer> entry : load.entrySet()) {
                load.put(entry.getKey(), Math.min(1200, (int) (entry.getValue() * loadFactor)));
            }
            LoginServer.getInstance().setLoad(load);
        } catch (RemoteException ex) {
            LoginServer.getInstance().reconnectWorld();
        }
    }
