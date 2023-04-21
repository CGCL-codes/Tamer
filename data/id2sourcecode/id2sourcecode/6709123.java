    @Override
    public void run() {
        running = true;
        try {
            terminal.writeTo("\nStarting master channel...\n");
            ss = new ServerSocket(port);
            terminal.writeTo(Colour.colourise("\nWITNA Channel v" + version, Colour.cyan));
            terminal.writeTo(Colour.colourise("\n-----", Colour.white));
            terminal.writeTo(Colour.colourise("\n-", Colour.white) + Colour.colourise("Channel     : ", Colour.cyan) + Colour.colourise("MASTER", Colour.yellow));
            terminal.writeTo(Colour.colourise("\n-", Colour.white) + Colour.colourise("Global ip   : ", Colour.cyan));
            String globalip = getGlobalIP();
            String localip = getLocalIP();
            terminal.writeTo(Colour.colourise(globalip + ":" + port, Colour.yellow));
            terminal.writeTo(Colour.colourise("\n-", Colour.white) + Colour.colourise("Local  ip   : ", Colour.cyan) + Colour.colourise(localip + ":" + port, Colour.yellow));
            String status = getNetStatus(globalip, localip);
            terminal.writeTo(Colour.colourise("\n-", Colour.white) + Colour.colourise("Connection  : ", Colour.cyan) + Colour.colourise(status, Colour.yellow) + Colour.colourise("\n", Colour.grey));
            listener = new MasterChannelServer(ss, sockets, motd);
            listener.start();
            int cycles = 0;
            while (running) {
                for (int x = 0; x < sockets.size(); x++) {
                    if (!sockets.get(x).isConnected()) {
                        sockets.remove(x);
                    }
                }
                boolean incorrectPass = false;
                int pass = 0;
                while (!incorrectPass && !UpdateChannelList.update(adminName, adminPass, getChannelString(), globalip)) {
                    Thread.sleep(5000);
                    terminal.writeTo("\nConnection error: trying again..");
                    if (pass == 3) {
                        incorrectPass = true;
                        terminal.writeTo("\nConnection failed three times in a row." + " Username and/or password might be bad.");
                    }
                }
                if (!incorrectPass) {
                    terminal.writeTo("\nChannel listing updated.");
                }
                Thread.sleep(25000);
                cycles++;
                if (cycles == 15) {
                    cycles = 0;
                    String newip = getGlobalIP();
                    if (!newip.equals(globalip)) {
                        globalip = newip;
                        terminal.writeTo("\nGlobal IP has changed. Your new IP address is " + globalip + ".");
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
