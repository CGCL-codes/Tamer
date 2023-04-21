    private void createAccountButton_actionPerformed(ActionEvent e, boolean saveLoginBoxStatus) {
        final String username = usernameField.getText();
        final String password = new String(passwordField.getPassword());
        final String passwordretype = new String(passwordField.getPassword());
        if (!password.equals(passwordretype)) {
            JOptionPane.showMessageDialog(owner, "The passwords do not match. Please retype both.", "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final String email = emailField.getText();
        final String server = serverField.getText();
        int port = 32160;
        final int finalPort;
        final ProgressBar progressBar = new ProgressBar(owner);
        try {
            port = Integer.parseInt(serverPortField.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(owner, "That is not a valid port number. Please try again.", "Invalid Port", JOptionPane.WARNING_MESSAGE);
            return;
        }
        finalPort = port;
        Thread m_connectionThread = new Thread() {

            @Override
            public void run() {
                progressBar.start();
                setVisible(false);
                try {
                    client.connect(server, finalPort, true);
                    progressBar.step();
                } catch (Exception ex) {
                    progressBar.cancel();
                    setVisible(true);
                    JOptionPane.showMessageDialog(owner, "Midhedava cannot connect to the Internet. Please check that your connection is set up and active, then try again.");
                    ex.printStackTrace();
                    return;
                }
                try {
                    if (client.createAccount(username, password, email) == false) {
                        String result = client.getEvent();
                        if (result == null) {
                            result = "The server is not responding. Please check that it is online, and that you supplied the correct details.";
                        }
                        progressBar.cancel();
                        setVisible(true);
                        JOptionPane.showMessageDialog(owner, result, "Error Creating Account", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        progressBar.step();
                        progressBar.finish();
                        client.setUserName(username);
                    }
                } catch (ariannexpTimeoutException ex) {
                    progressBar.cancel();
                    setVisible(true);
                    JOptionPane.showMessageDialog(owner, "Unable to connect to server to create your account. The server may be down or, if you are using a custom server, you may have entered its name and port number incorrectly.", "Error Creating Account", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    if (client.login(username, password) == false) {
                        String result = client.getEvent();
                        if (result == null) {
                            result = "Unable to connect to server. The server may be down or, if you are using a custom server, you may have entered its name and port number incorrectly.";
                        }
                        progressBar.cancel();
                        setVisible(true);
                        JOptionPane.showMessageDialog(owner, result, "Error Logging In", JOptionPane.ERROR_MESSAGE);
                    } else {
                        progressBar.step();
                        progressBar.finish();
                        setVisible(false);
                        owner.setVisible(false);
                        midhedava.doLogin = true;
                    }
                } catch (ariannexpTimeoutException ex) {
                    progressBar.cancel();
                    setVisible(true);
                    JOptionPane.showMessageDialog(owner, "Unable to connect to the server. The server may be down or, if you are using a custom server, you may have entered its name and port number incorrectly.", "Error Logging In", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        m_connectionThread.start();
    }
