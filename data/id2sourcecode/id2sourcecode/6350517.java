    public void run() {
        Notification notif;
        SMTPClient client = null;
        try {
            running = true;
            while (running) {
                synchronized (this) {
                    if (queue.size() == 0) {
                        wait();
                    }
                    notif = queue.removeFirst();
                }
                if (notif == null) {
                    continue;
                }
                String header = new SimpleSMTPHeader(sender, recipient, notif.subject).toString();
                client = new SMTPClient();
                client.connect(host);
                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                    throw new Exception("SMTP server " + host + " refused connection.");
                }
                if (!client.login()) {
                    throw new Exception("SMTP: Problem logging in: error #" + client.getReplyCode() + " " + client.getReplyString());
                }
                if (!client.setSender(sender)) {
                    throw new Exception("SMTP: Problem setting sender to " + sender + ": error #" + client.getReplyCode() + " " + client.getReplyString());
                }
                if (!client.addRecipient(recipient)) {
                    throw new Exception("SMTP: Problem adding recipient " + recipient + ": error #" + client.getReplyCode() + " " + client.getReplyString());
                }
                if (!client.sendShortMessageData(header + notif.body)) {
                    throw new Exception("Problem sending notification : error #" + client.getReplyCode() + " " + client.getReplyString());
                }
                try {
                    client.logout();
                    client.disconnect();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            try {
                if (client != null) {
                    client.logout();
                    client.disconnect();
                }
            } catch (Exception f) {
            }
            Logger.enableNotifications(false);
            Logger.log(e);
        }
    }
