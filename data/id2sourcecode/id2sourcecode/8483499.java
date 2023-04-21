    public final void send(final IMessage msg, final ClientSession player) {
        try {
            Iterator<ClientSession> i = getChannel().getSessions();
            StringBuffer logMsg = new StringBuffer(this.getChannel().getName());
            logMsg.append("->Enviando msg ").append(msg.getType()).append(" los usuarios: ");
            boolean empty = true;
            while (i.hasNext()) {
                empty = false;
                logMsg.append(" ");
                logMsg.append(i.next().getName());
            }
            if (empty) {
                logMsg.append("-nadie-");
            }
            logger.info(logMsg.toString());
            getChannel().send(player, msg.toByteBuffer());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
