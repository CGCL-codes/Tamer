    public void deliver(User user, String subject, String message) {
        if (user instanceof BasicUser) {
            BasicUser basicUser = (BasicUser) user;
            String recipient = basicUser.getEmail();
            if (recipient != null) {
                if (log.isDebugEnabled()) log.debug("delivery To:" + basicUser.getName() + " Sub:" + subject);
                try {
                    SimpleSMTPHeader header = getHeader(recipient, subject);
                    SMTPClient client = new SMTPClient();
                    client.connect(server);
                    if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                        client.disconnect();
                        System.err.println("SMTP server refused connection.");
                        System.exit(1);
                    }
                    client.login();
                    setClientHeaders(client, recipient);
                    writeMessage(client, header, message);
                    client.logout();
                    client.disconnect();
                    if (log.isDebugEnabled()) log.debug("Sent sucessfully");
                } catch (IOException e) {
                    log.error("Failed to send email to " + user.getName(), e);
                }
            } else {
                log.error("Cant send email to " + basicUser.getName() + " - dont have an email address");
            }
        } else {
            log.error("Cant send email to " + user.getName() + " - cant work out his email address.");
        }
    }
