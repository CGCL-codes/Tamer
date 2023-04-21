    private synchronized void connect() {
        if (shutdown) return;
        attempts++;
        try {
            if (name_servers.size() == 0) Log.sysOut("Intermud3", "No I3 routers defined in coffeemud.ini file."); else {
                if (CMProps.getVar(CMProps.SYSTEM_ADMINEMAIL).indexOf("@") < 0) Log.errOut("Intermud", "Please set ADMINEMAIL in your coffeemud.ini file.");
                Vector connectionStatuses = new Vector(name_servers.size());
                for (int i = 0; i < name_servers.size(); i++) {
                    NameServer n = (NameServer) name_servers.elementAt(i);
                    try {
                        connection = new Socket(n.ip, n.port);
                        output = new DataOutputStream(connection.getOutputStream());
                        send("({\"startup-req-3\",5,\"" + intermud.getMudName() + "\",0,\"" + n.name + "\",0," + password + "," + muds.getMudListId() + "," + channels.getChannelListId() + "," + intermud.getMudPort() + ",0,0,\"" + intermud.getMudVersion() + "\",\"" + intermud.getMudVersion() + "\",\"" + intermud.getMudVersion() + "\",\"CoffeeMud\"," + "\"" + intermud.getMudState() + "\",\"" + CMProps.getVar(CMProps.SYSTEM_ADMINEMAIL).toLowerCase() + "\",([" + "\"who\":1,\"finger\":1,\"channel\":1,\"tell\":1,\"locate\":1,]),([]),})");
                    } catch (java.io.IOException e) {
                        connectionStatuses.addElement(n.ip + ": " + n.port + ": " + e.getMessage());
                        continue;
                    }
                    connected = true;
                    input_thread = new Thread(this);
                    input_thread.setDaemon(true);
                    input_thread.setName("Intermud");
                    input_thread.start();
                    Enumeration e = intermud.getChannels();
                    while (e.hasMoreElements()) {
                        String chan = (String) e.nextElement();
                        send("({\"channel-listen\",5,\"" + intermud.getMudName() + "\",0,\"" + n.name + "\",0,\"" + chan + "\",1,})");
                    }
                    Log.sysOut("Intermud3", "I3 client connection: " + n.ip + "@" + n.port);
                    break;
                }
                if (!connected) for (int e = 0; e < connectionStatuses.size(); e++) Log.errOut("Intermud", (String) connectionStatuses.elementAt(e));
            }
        } catch (Exception e) {
            try {
                Thread.sleep(((long) attempts) * 100l);
            } catch (InterruptedException ignore) {
                if (shutdown) {
                    Log.sysOut("Intermud", "Shutdown!");
                    return;
                }
            }
            connect();
        }
    }
