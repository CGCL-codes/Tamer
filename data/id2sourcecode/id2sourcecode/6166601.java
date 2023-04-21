        public void actionPerformed(final ActionEvent e) {
            if (loginDialog == null) {
                loginDialog = new LoginDialog(VBoxGUI.this, (String) getValue(Preferences.HOST), (String) getValue(Preferences.PORT), (String) getValue(Preferences.USER), (String) getValue(Preferences.PASSWORD));
            } else {
                loginDialog.reset();
            }
            Object val = getValue(Preferences.AUTOLOGON);
            if ((val instanceof Boolean) && (Boolean.FALSE.equals((Boolean) val) || !isFirstLogin)) {
                loginDialog.show();
            } else {
                loginDialog.doClickOK();
            }
            isFirstLogin = false;
            if (!loginDialog.hasBeenCanceled()) {
                String host = loginDialog.getHost();
                PasswordAuthentication authInfo = loginDialog.getAuthInfo();
                int port = loginDialog.getPort();
                try {
                    VBoxGUI.this.proxy.connect(host, port);
                    VBoxGUI.this.connected = true;
                    String user = authInfo.getUserName();
                    String pass = new String(authInfo.getPassword());
                    VBoxGUI.this.proxy.login(user, pass);
                    msgFrame.setTitle(messageBundle.getString("connected") + " " + user + "@" + host + ":" + port);
                    this.setEnabled(false);
                    VBoxGUI.this.logoutAction.setEnabled(true);
                    VBoxGUI.this.msgTable.setEnabled(true);
                    VBoxGUI.this.listAction.setEnabled(true);
                    if (loadListButton == null) {
                        loadListButton = getLoadListButton();
                    }
                    if (loadListButton != null) {
                        loadListButton.doClick();
                    }
                    msgTable.revalidate();
                    putValue(Preferences.HOST, host);
                    putValue(Preferences.PORT, String.valueOf(port));
                    putValue(Preferences.USER, user);
                    if (getValue(Preferences.PASSWORD) != null) {
                        putValue(Preferences.PASSWORD, pass);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showErrorDialog("loginError.title", null, "loginError.message", ex);
                }
            }
        }
