    public static void sendMail(String subject, String toAddress, String mailContents, String organization) {
        String smtpserver = ServerConsoleServlet.getConfigByTagName("SMTPServer");
        String smtport = ServerConsoleServlet.getConfigByTagName("SMTPPort");
        String fromAddress = ServerConsoleServlet.getOrganizationConfigByTagName("FromAddress", organization);
        if ((fromAddress == null) || (fromAddress.length() == 0)) fromAddress = ServerConsoleServlet.getConfigByTagName("FromAddress");
        if (((smtpserver == null) || (smtpserver.length() == 0)) || ((toAddress == null) || (toAddress.length() == 0)) || ((smtport == null) || (smtport.length() == 0)) || ((fromAddress == null) || (fromAddress.length() == 0))) {
            ServerConsoleServlet.printSystemLog("invalid input", ServerConsoleServlet.LOG_ERROR);
            return;
        }
        if (mailContents == null) mailContents = new String("");
        try {
            toAddress = toAddress.replaceAll(" ", "");
            String[] toAddresses = toAddress.split(",");
            StringBuffer mailHeader = new StringBuffer();
            mailHeader.append("Subject: " + new String(subject.getBytes("ISO-2022-JP")) + "\r\n");
            mailHeader.append("To: " + toAddress + "\r\n");
            mailHeader.append("MIME-Version: 1.0\r\n");
            mailHeader.append("Content-Type: text/plain; charset=\"ISO-2022-JP\"\r\n");
            mailHeader.append("X-Mailer: ShareFast Server\r\n");
            SMTPClient smtpc = new SMTPClient();
            smtpc.connect(smtpserver);
            smtpc.login();
            smtpc.setDefaultPort(Integer.parseInt(smtport));
            smtpc.sendSimpleMessage(fromAddress, toAddresses, mailHeader.toString() + "\r\n" + new String(mailContents.getBytes("ISO-2022-JP")));
            smtpc.disconnect();
        } catch (IOException ioe) {
            ServerConsoleServlet.printSystemLog("Cannot connect to SMTP server = " + smtpserver, ServerConsoleServlet.LOG_WARN);
            ServerConsoleServlet.printSystemLog(ioe.toString() + " " + ioe.getMessage(), ServerConsoleServlet.LOG_ERROR);
        } catch (Exception e) {
            ServerConsoleServlet.printSystemLog(e.toString() + " " + e.getMessage(), ServerConsoleServlet.LOG_ERROR);
            e.printStackTrace();
        }
    }
