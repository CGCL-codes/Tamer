    public void _sendMail(String mailFrom, String mailTo, String subject, String message) throws Exception {
        try {
            String host = getString("/OpenForum/Actions/SendEMail", "host.txt");
            if (host.length() == 0 || host.equals("smtp.server")) {
                return;
            }
            mailTo = getString("/Admin/Users/" + mailTo + "/private", "email.txt");
            mailFrom = getString("/Admin/Users/" + mailFrom + "/private", "email.txt");
            SMTPClient client = (SMTPClient) getClass().getClassLoader().loadClass("org.apache.commons.net.smtp.SMTPClient").newInstance();
            client.connect(host);
            boolean result = client.login();
            client.setSender(mailFrom);
            client.addRecipient(mailTo);
            SimpleSMTPHeader header = new SimpleSMTPHeader(mailFrom, mailTo, subject);
            result = client.sendShortMessageData(header.toString() + message);
            client.logout();
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
