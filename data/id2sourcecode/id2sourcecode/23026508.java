    private void writeUsingSMTP(MessageContext msgContext, String smtpHost, String sendFrom, String replyTo, String subject, Message output) throws Exception {
        SMTPClient client = new SMTPClient();
        client.connect(smtpHost);
        System.out.print(client.getReplyString());
        int reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            AxisFault fault = new AxisFault("SMTP", "( SMTP server refused connection )", null, null);
            throw fault;
        }
        client.login(smtpHost);
        System.out.print(client.getReplyString());
        reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            AxisFault fault = new AxisFault("SMTP", "( SMTP server refused connection )", null, null);
            throw fault;
        }
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sendFrom));
        msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(replyTo));
        msg.setDisposition(MimePart.INLINE);
        msg.setSubject(subject);
        ByteArrayOutputStream out = new ByteArrayOutputStream(8 * 1024);
        output.writeTo(out);
        msg.setContent(out.toString(), output.getContentType(msgContext.getSOAPConstants()));
        ByteArrayOutputStream out2 = new ByteArrayOutputStream(8 * 1024);
        msg.writeTo(out2);
        client.setSender(sendFrom);
        System.out.print(client.getReplyString());
        client.addRecipient(replyTo);
        System.out.print(client.getReplyString());
        Writer writer = client.sendMessageData();
        System.out.print(client.getReplyString());
        writer.write(out2.toString());
        writer.flush();
        writer.close();
        System.out.print(client.getReplyString());
        if (!client.completePendingCommand()) {
            System.out.print(client.getReplyString());
            AxisFault fault = new AxisFault("SMTP", "( Failed to send email )", null, null);
            throw fault;
        }
        System.out.print(client.getReplyString());
        client.logout();
        client.disconnect();
    }
