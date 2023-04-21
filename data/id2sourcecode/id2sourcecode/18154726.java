    public void doTestJabber(TestType testType) throws Exception {
        ConnectionConfiguration config = new ConnectionConfiguration(JABBER_SERVER, JABBER_PORT, JABBER_SERVICE);
        {
            config.setSASLAuthenticationEnabled(JABBER_SASL_AUTHENTICATION_ENABLED);
        }
        final XMPPConnection conn = new XMPPConnection(config);
        conn.connect();
        discoManager = ServiceDiscoveryManager.getInstanceFor(conn);
        conn.addConnectionListener(new ConnectionListener() {

            public void connectionClosed() {
                log.info("            Connection closed");
            }

            public void connectionClosedOnError(Exception e) {
                log.info("            Connection closed on error", e);
            }

            public void reconnectingIn(int seconds) {
                log.info("            ReConnecting...");
            }

            public void reconnectionSuccessful() {
                log.info("            ReConnecting ok");
            }

            public void reconnectionFailed(Exception e) {
                log.info("            ReConnecting error", e);
            }
        });
        conn.addPacketListener(new PacketListener() {

            public void processPacket(Packet packet) {
                log.trace("                >>> Incoming packet : " + packet.getClass() + " -> " + packet.toXML());
                if (packet instanceof Presence) {
                    Presence presence = (Presence) packet;
                    if (presence.getType() == Presence.Type.subscribe) {
                        Presence response = new Presence(Presence.Type.subscribed);
                        response.setTo(presence.getFrom());
                        conn.sendPacket(response);
                        log.info("            Subscribed for " + presence.getFrom());
                    } else if (presence.getType() == Presence.Type.unsubscribe) {
                        Presence response = new Presence(Presence.Type.unsubscribed);
                        response.setTo(presence.getFrom());
                        conn.sendPacket(response);
                        log.info("            UnSubscribed for " + presence.getFrom());
                    } else {
                        log.debug("            Presence : " + presence);
                    }
                }
            }
        }, null);
        conn.addPacketWriterListener(new PacketListener() {

            public void processPacket(Packet packet) {
                log.trace("                <<< Outgoing packet : " + packet.toXML());
            }
        }, null);
        conn.login(JABBER_LOGIN, JABBER_PASSWORD, JABBER_CLIENT);
        log.info("Login succesfull");
        log.info("    User : " + conn.getUser());
        Roster roster = conn.getRoster();
        roster.addRosterListener(new RosterListener() {

            public void entriesAdded(Collection<String> addresses) {
                log.info("            Roster Entries added : " + addresses);
            }

            public void entriesDeleted(Collection<String> addresses) {
                log.info("            Roster Entries deleted : " + addresses);
            }

            public void entriesUpdated(Collection<String> addresses) {
                log.info("            Roster Entries updated : " + addresses);
            }

            public void presenceChanged(Presence presence) {
                log.info("            Presence changed: " + presence.getFrom() + " " + presence);
            }
        });
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        {
            Presence presence = new Presence(Presence.Type.available);
            presence.setStatus("Hello! I am here! И русский.");
            conn.sendPacket(presence);
        }
        if (false) {
            showRosterInfo(roster);
            log.info("Subscribe to self...");
            Presence response = new Presence(Presence.Type.subscribe);
            response.setTo(JABBER_LOGIN);
            conn.sendPacket(response);
            {
                Chat chatLocalUser = conn.getChatManager().createChat(JABBER_LOGIN, new SimpleMessageListener());
                chatLocalUser.sendMessage("Self First message");
                chatLocalUser.sendMessage("Self Второе сообщение");
            }
            {
                Chat chatLocalUser = conn.getChatManager().createChat("pavel.moukhataev@gmail.com", new SimpleMessageListener());
                chatLocalUser.sendMessage("First message");
                chatLocalUser.sendMessage("Второе сообщение");
            }
        }
        if (true) {
            String gatewayJid = "icq.aftar.ru";
            discoverItems("aftar.ru", null);
            discoverIdentities(gatewayJid, null);
            if (true) return;
            discoverItems(JABBER_LOGIN, null);
            logoutFromGateway(conn, "icq.freeside.ru");
            unregisterOnGateway(conn, "icq.freeside.ru");
            logoutFromGateway(conn, gatewayJid);
            unregisterOnGateway(conn, gatewayJid);
        }
        if (testType == TestType.allServers) {
            for (String icqGateway : ICQ_GATEWAYS) {
                try {
                    testAllIcqServers(conn, icqGateway);
                } catch (Exception e) {
                    log.error("Error testing " + icqGateway, e);
                }
            }
        } else {
            testCaseIcqMain(testType, conn, ICQ_GATEWAY);
        }
        waitPressKey("disconnect");
        conn.disconnect();
        waitPressKey("EXIT");
    }
