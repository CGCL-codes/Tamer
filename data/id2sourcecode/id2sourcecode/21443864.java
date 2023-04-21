    public void doTestJabber() throws Exception {
        String[] icqAccountsStrings = TEST_PROPERTIES.getProperty("icq.users").split(";");
        ICQ_ACCOUNTS = new String[icqAccountsStrings.length][];
        for (int i = 0; i < icqAccountsStrings.length; i++) {
            String icqAccountsString = icqAccountsStrings[i];
            String[] icqAccountStrings = icqAccountsString.split(":", 2);
            ICQ_ACCOUNTS[i] = icqAccountStrings;
        }
        ConnectionConfiguration config = new ConnectionConfiguration(JABBER_SERVER, JABBER_PORT, JABBER_SERVICE);
        config.setSASLAuthenticationEnabled(JABBER_SASL_AUTHENTICATION_ENABLED);
        conn = new XMPPConnection(config);
        conn.connect();
        conn.addPacketListener(new PacketListener() {

            public void processPacket(Packet packet) {
                if (packet instanceof Presence) {
                    presenceExists = true;
                    Presence presence = (Presence) packet;
                    if (presence.getType() == Presence.Type.subscribe) {
                        Presence response = new Presence(Presence.Type.subscribed);
                        response.setTo(presence.getFrom());
                        conn.sendPacket(response);
                        log.info("            Subscribed for " + presence.getFrom());
                        if (!presence.getFrom().endsWith(gatewayJid)) {
                            log.warn("Unknown subscription : " + presence.getFrom());
                        }
                    } else if (presence.getType() == Presence.Type.unsubscribe) {
                        Presence response = new Presence(Presence.Type.unsubscribed);
                        response.setFrom(conn.getUser());
                        response.setTo(presence.getFrom());
                        conn.sendPacket(response);
                        log.info("            UnSubscribed for " + presence.getFrom());
                    } else {
                        log.trace("            Presence [" + presence.getFrom() + "] : " + presence.toXML());
                        presenceData.put(presence.getFrom().replaceAll("/.*", ""), presence);
                    }
                }
            }
        }, null);
        conn.addPacketWriterListener(new PacketListener() {

            public void processPacket(Packet packet) {
            }
        }, null);
        conn.login(JABBER_LOGIN, JABBER_PASSWORD, JABBER_CLIENT);
        Roster roster = conn.getRoster();
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        {
            Presence presence = new Presence(Presence.Type.available);
            presence.setStatus("Hello! I am here! Testing ICQ Gateways...");
            conn.sendPacket(presence);
        }
        discoManager = ServiceDiscoveryManager.getInstanceFor(conn);
        simpleMessageListener.setDaemon(true);
        simpleMessageListener.start();
        for (String jabberGateway : ICQ_GATEWAYS) {
            try {
                if (testJabberGateway(jabberGateway) == 0) {
                    testResults.put(jabberGateway, EnumSet.of(ServerType.nogateway));
                }
            } catch (Exception e) {
                testResults.put(jabberGateway, EnumSet.of(ServerType.unavailable));
            }
        }
        conn.disconnect();
        log.info("Test results...");
        for (Map.Entry<String, Set<ServerType>> stringSetEntry : testResults.entrySet()) {
            log.info("Server : " + stringSetEntry.getKey() + ", result : " + stringSetEntry.getValue());
        }
        log.info("Alive servers...");
        for (String aliveServer : aliveServers) {
            log.info(aliveServer);
        }
    }
