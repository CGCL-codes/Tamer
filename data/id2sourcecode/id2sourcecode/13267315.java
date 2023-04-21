    public void send() throws AddressException, MessagingException, SocketException, IOException {
        if (set != null) {
            log.info("Building error report for message " + set.hashCode());
            System.out.println("Building error report for message " + set.hashCode());
        } else {
            log.info("Building error report");
            System.out.println("Building error");
        }
        int reply;
        SMTPClient client = new SMTPClient();
        client.connect(LightAttachment.config.getString("report.smtp"), 25);
        reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            log.warn("Fail to send error report");
        } else {
            if (client.login(LightAttachment.config.getString("hostname"))) {
                boolean tok = client.setSender(LightAttachment.config.getString("report.mailfrom"));
                StringTokenizer st = new StringTokenizer(LightAttachment.config.getString("report.mailto"), " ");
                while (st.hasMoreTokens()) {
                    String rcpt = st.nextToken();
                    if (!client.addRecipient(rcpt)) tok = false;
                }
                if (tok) {
                    Writer w = client.sendMessageData();
                    if (w != null) {
                        String head = "To: " + LightAttachment.config.getString("report.mailto").replace(" ", ",") + "\r\n";
                        if (set != null) head += "Subject: LightAttachment Error Report for Message " + set.hashCode() + "\r\n"; else head += "Subject: LightAttachment Error Report\r\n";
                        String stack = "";
                        if (exception != null) {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            exception.printStackTrace(pw);
                            stack = sw.getBuffer().toString();
                        }
                        if (set != null) {
                            head += "The message " + set.hashCode() + " from " + set.getFrom() + " to " + set.getTo() + " was processed with errors by LightAttachment on " + LightAttachment.config.getString("hostname") + " for the following reason(s): \n\n";
                        } else {
                            head += "A serious error was encountered on " + LightAttachment.config.getString("hostname") + " for the following reason(s): \n\n";
                        }
                        if (message != null) {
                            if (exception != null) w.write(head + message + "\n\n" + stack); else w.write(head + message);
                        } else if (exception != null) w.write(head + "\n\n" + stack); else w.write(head);
                        w.close();
                        if (set != null) {
                            log.info("Error report of message " + set.hashCode() + " delivered to " + LightAttachment.config.getString("report.smtp"));
                            System.out.println("Error report of message " + set.hashCode() + " delivered to " + LightAttachment.config.getString("report.smtp"));
                        } else {
                            log.info("Error report delivered to " + LightAttachment.config.getString("report.smtp"));
                            System.out.println("Error report  delivered to " + LightAttachment.config.getString("report.smtp"));
                        }
                        if (!client.completePendingCommand()) {
                            client.disconnect();
                        }
                        client.disconnect();
                    }
                }
            } else {
                client.disconnect();
                log.warn("Fail to send error report");
            }
        }
    }
