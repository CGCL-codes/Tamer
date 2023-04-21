    public void start() {
        try {
            ServerSocket server = new ServerSocket(portNb);
            for (int i = 0; i < wms.size(); i++) {
                try {
                    wm.loadFromXML((String) wms.get(i));
                } catch (Exception e) {
                    ReasoningServer.writeLog("Couldn't load working memory from file " + wms.get(i));
                }
            }
            TimerThread timer = new TimerThread();
            timer.start();
            ReasoningServer.writeLog(ServerMessagesResources.getString("sies.server.ready"));
            while (!killyourself) {
                try {
                    Socket client = server.accept();
                    if (killyourself) {
                        break;
                    }
                    ClientThread t = new ClientThread(new SocketIOManager(client));
                    t.start();
                    synchronized (clients) {
                        clients.add(t);
                    }
                } catch (IOException e) {
                    ReasoningServer.writeLog("Failed to accept connection on port " + portNb);
                    killyourself = true;
                }
            }
        } catch (IOException e) {
            System.out.println("error on port " + portNb);
        }
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i) != null) {
                ClientThread t = (ClientThread) clients.get(i);
                try {
                    t.closeConnections();
                } catch (IOException e) {
                    ReasoningServer.writeLog("Cannot close all connections.");
                }
            }
        }
    }
