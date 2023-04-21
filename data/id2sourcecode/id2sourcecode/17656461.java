    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.loginButton) {
            if (this.ip == null) {
                this.ip = serverField.getText();
                this.port = Integer.parseInt(portField.getText());
                this.panel.removeAll();
                createLoginWindow();
                this.pack();
            } else {
                String userAccount = accountField.getText();
                String password = passwordField.getText();
                if (!threadLaunched) {
                    client.network = new NetworkConnection(this.ip, this.port, this.client);
                    client.network.connect();
                    client.network.start();
                }
                client.network.connectUser(userAccount, MD5.digest(password));
            }
        }
        if (event.getSource() == this.registerButton) {
            if (this.ip == null) {
                this.ip = serverField.getText();
                this.port = Integer.parseInt(portField.getText());
                this.panel.removeAll();
                createRegistrationWindow();
                this.pack();
            } else {
                String userAccount = accountField.getText();
                String password1 = passwordField.getText();
                String password2 = password2Field.getText();
                String realName = realNameField.getText();
                String email = emailField.getText();
                short timeZone = new Short(timeZoneField.getText());
                if (password1.equals(password2)) {
                    if (!threadLaunched) {
                        client.network = new NetworkConnection(this.ip, this.port, this.client);
                        client.network.connect();
                        client.network.start();
                    }
                    client.network.register(userAccount, MD5.digest(password1), realName, email, timeZone);
                    this.panel.removeAll();
                    createLoginWindow();
                    this.pack();
                }
            }
        }
    }
