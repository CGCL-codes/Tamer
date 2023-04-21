            public void actionPerformed(ActionEvent e) {
                String username = txtUserName.getText();
                String psw = txtPsw.getText();
                String server = txtServer.getText();
                if (TheGlobal.connection != null && TheGlobal.connection.isConnected()) {
                    TheGlobal.connection.disconnect();
                }
                TheGlobal.connection = new XMPPConnection(server);
                try {
                    TheGlobal.connection.connect();
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Can not connect to the server");
                    return;
                }
                try {
                    TheGlobal.connection.login(username, psw);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Wrong user name or password.");
                    return;
                }
                if (mainFrame != null) {
                    mainFrame.LoginOk();
                }
            }
