    @Override
    public void connect(String destiny, String typeDestiny) {
        ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        this.connection = new XMPPConnection(connConfig);
        try {
            logger.info("Conectando.....");
            connection.connect();
            if (this.pass != null) connection.login(userName, pass);
            logger.info("Conectado!!");
            this.status = XMPPPluginsCommunicator.OK_STATUS;
            Presence p = new Presence(Presence.Type.available);
            p.setMode(Presence.Mode.available);
            logger.info("Enviando presencia..");
            connection.sendPacket(p);
            logger.info("Presencia enviada!!");
            logger.info("Listening paquetes...");
            PacketFilter filter = new PacketTypeFilter(org.jivesoftware.smack.packet.Message.class);
            this.inListener = new PerfectDayPacketListener();
            this.chat = this.connection.getChatManager().createChat(destiny, (MessageListener) inListener);
            logger.info("registe packetFilter...");
            this.connection.addPacketListener(inListener, filter);
            logger.info("Registro de Listener!!!");
            Roster roster = connection.getRoster();
            for (RosterEntry entry : roster.getEntries()) {
                logger.info("roster: " + entry.getUser());
            }
        } catch (XMPPException ex) {
            org.apache.log4j.Logger.getLogger(XMPPPluginsCommunicator.class).error("XMPP Communication error", ex);
            this.status = XMPPPluginsCommunicator.FAULT_STATUS;
        }
    }
