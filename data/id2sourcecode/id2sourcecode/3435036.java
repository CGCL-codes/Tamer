    public void send(Session session) throws AddressException, MessagingException, SocketException, IOException {
        Message message = new MimeMessage(session);
        log.info("Building report message " + code);
        System.out.println("Building report message " + code);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        if (filename != null) {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDisposition("attachment");
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            if (filename.indexOf("\\") > -1) {
                filename = filename.substring(filename.lastIndexOf("\\") + 1);
            } else {
                filename = filename.substring(filename.lastIndexOf("/") + 1);
            }
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
        }
        message.setContent(multipart);
        int reply;
        SMTPClient client = new SMTPClient();
        client.connect(LightAttachment.config.getString("report.smtp"), 25);
        reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            log.warn("Fail to send activity report");
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
                        StringOutputStream sos = new StringOutputStream(new StringBuffer());
                        message.writeTo(sos);
                        String m = sos.getString();
                        w.write(m);
                        w.close();
                        log.info("Activity report delivered to " + LightAttachment.config.getString("report.smtp"));
                        System.out.println("Activity report  delivered to " + LightAttachment.config.getString("report.smtp"));
                        if (!client.completePendingCommand()) {
                            client.disconnect();
                        }
                        client.disconnect();
                    }
                }
            } else {
                client.disconnect();
                log.warn("Fail to send activity report");
            }
        }
    }
