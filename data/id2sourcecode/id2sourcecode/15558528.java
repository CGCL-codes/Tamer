    public void applyOptions(ServerConfigurationOptions sco) {
        if (opcionesServidor.sirveTelnet()) {
            if (elServidorTelnet == null) elServidorTelnet = new SimpleTelnetClientHandler((short) opcionesServidor.getPuertoTelnet());
        } else {
            elServidorTelnet = null;
        }
        if (opcionesServidor.sirveAge()) {
            if (elServidorAge == null) elServidorAge = new AGEClientHandler((short) opcionesServidor.getPuertoAge());
        } else {
            elServidorAge = null;
        }
        if (opcionesServidor.sirveIrc()) {
            java.util.List ircServerEntryList = opcionesServidor.getListaServidoresIrc();
            for (int i = 0; i < ircServerEntryList.size(); i++) {
                final IrcServerEntry ise = (IrcServerEntry) ircServerEntryList.get(i);
                Thread th = new Thread() {

                    public void run() {
                        try {
                            IrcAgeBot iab = new IrcAgeBot(ise.getServer(), ise.getPort(), ise.getNick(), ise.getChannels());
                            synchronized (theInstance) {
                                losBotsIrc.add(iab);
                            }
                        } catch (Exception e) {
                            if (logWin != null) {
                                logWin.writeGeneral("Exception found when trying to connect bot to server " + ise.getServer() + "\n");
                                logWin.writeGeneral(e + ":" + e.getMessage());
                                e.printStackTrace();
                                Debug.println("HALCYON\nAND ON\nAND ON\n");
                            }
                        }
                    }
                };
                th.setPriority(Thread.MIN_PRIORITY);
                th.start();
            }
        } else {
            losBotsIrc.clear();
            partidasIrc.clear();
        }
    }
