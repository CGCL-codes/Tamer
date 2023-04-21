    private void login() {
        final Protocol prtcl = Protocol.instance;
        Logincommand logincmd = new Logincommand();
        User u = new User();
        u.setName(loginDlg.lineedit_name.text());
        email = prtcl.generateEmailFromName(u.getName());
        u.setEmail(email);
        logincmd.setCurrentUser(u);
        logincmd.setSHA1Password(prtcl.encryptSHA1(loginDlg.lineedit_password.text()));
        userPassword = loginDlg.lineedit_password.text();
        loginDlg.lineedit_password.setText("");
        try {
            prtcl.serializeMessageObject(serverConnection.getOutputStream(), ProtocolCommand.LoginToServer, ProtocolMessageType.Request, logincmd, "pdc.xml.logincommand");
            ProtocolMessage msg = prtcl.deserializeMessageReader(serverConnection.getInputStream());
            if (msg.getProtocolCommand() == ProtocolCommand.LoginToServer && msg.getType() == ProtocolMessageType.Response) {
                BufferedReader br = msg.getDataStream();
                loggedIn = new Boolean(br.readLine());
                System.out.println("loggedIn? " + loggedIn);
                if (loggedIn) {
                    qloginDlg.done(DialogCode.Accepted.value());
                    receivedConfig = new File("receivedConfiguration.xml");
                    BufferedWriter conf_writer = new BufferedWriter(new FileWriter(receivedConfig));
                    conf_writer.write(prtcl.readOutXML(msg.getDataStream()).toString());
                    conf_writer.close();
                    parseConfigScript(getConfigScript());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
