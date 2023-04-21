    public void reconnect() throws SocketException, IOException {
        int reply;
        client.connect(SMTPAddress, port);
        this.connected = System.currentTimeMillis();
        log.info("SMTPPostfixOutputConnector(" + this.hashCode() + ") reconnected to " + client.getReplyString().replace("\r\n", "").replace("220 ", "") + " in " + (connected - created) + " ms");
        reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            log.error("(" + this.hashCode() + ") Postfix SMTP server refused connection");
            SendErrorReportThread sert = new SendErrorReportThread(null, "Error while initiating connection to Postfix: Postfix SMTP server refused connection (" + this.hashCode() + ")", null);
            sert.start();
            shutdown();
        } else {
            if (client.login()) {
                log.info("SMTPPostfixOutputConnector(" + this.hashCode() + ") logged in");
            } else {
                client.disconnect();
                log.error("(" + this.hashCode() + ") Couldn't login Postfix SMTP Server");
                SendErrorReportThread sert = new SendErrorReportThread(null, "Error while initiating connection to Postfix: Couldn't login Postfix SMTP Server (" + this.hashCode() + ")", null);
                sert.start();
                shutdown();
            }
        }
    }
