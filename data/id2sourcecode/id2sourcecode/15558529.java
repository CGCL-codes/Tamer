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
