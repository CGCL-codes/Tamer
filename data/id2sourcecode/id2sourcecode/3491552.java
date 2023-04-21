    public static void send(InternetAddress ia, Email email) throws EmailException {
        EmailException ee = new EmailException("Invalid email address: " + ia.getAddress());
        String[] ss = ia.getAddress().split("@");
        if (ss.length != 2) {
            throw ee;
        }
        List<String> hosts;
        try {
            hosts = MXLookup.lookup(ss[1]);
        } catch (NamingException e) {
            throw new EmailException(e);
        }
        for (String host : hosts) {
            try {
                PrivateAccessUtils.setFieldValue(email, "session", null);
            } catch (Exception e) {
                throw new EmailException("failed to clear session", e);
            }
            try {
                email.setHostName(host);
                email.buildMimeMessage();
                MimeMessage message = email.getMimeMessage();
                try {
                    Transport.send(message, new InternetAddress[] { ia });
                } catch (Throwable t) {
                    String msg = "Sending the email to the following server failed : " + email.getHostName() + ":" + email.getSmtpPort();
                    throw new EmailException(msg, t);
                }
                return;
            } catch (EmailException e) {
                ee = e;
            }
        }
        throw ee;
    }
