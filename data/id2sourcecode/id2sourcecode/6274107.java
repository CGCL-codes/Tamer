    public void connect(String username, String password) {
        ConnectionConfiguration connConfig = null;
        if (tfProxyUserName.getText().trim().equals("")) {
            connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        } else {
            ProxyInfo pi = new ProxyInfo(ProxyInfo.ProxyType.HTTP, tfProxyHost.getText(), Integer.parseInt(tfProxyPort.getText()), tfProxyUserName.getText(), String.valueOf(tfProxyPassword.getPassword()));
            connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com", pi);
        }
        connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to connect to Google talk");
            jButton2.setEnabled(true);
            jButton3.setEnabled(false);
        }
        try {
            connection.login(username, password);
            System.out.println("Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            jButton1.setEnabled(true);
        } catch (Exception ex) {
            jLabel2.setText("INVALID login details for " + username);
            JOptionPane.showMessageDialog(null, "Google talk authentication failed, this may be due to Internet failure or wrong user name and password");
            jButton1.setEnabled(false);
            jButton2.setEnabled(true);
            jButton3.setEnabled(false);
        }
        ChatManager chatmanager = connection.getChatManager();
        Chat chat = chatmanager.createChat("ngl.atyourservice@gmail.com", new MessageListener() {

            @Override
            public void processMessage(Chat chat, Message msg) {
                jTextArea1.append("Verus NGL support said: " + msg.getBody() + "\n");
            }
        });
        this.chat = chat;
        jButton2.setEnabled(false);
        jLabel2.setText("You are logged in as " + username);
        NGLPreferences.getUserRoot().put("GoogleUserName", tfUsername.getText());
        jButton3.setEnabled(true);
        jTextField1.requestFocus();
    }
