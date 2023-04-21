    protected void connect(Profile profile) {
        final ProgressBar progressBar = new ProgressBar(this);
        progressBar.start();
        setEnabled(false);
        try {
            client.connect(profile.getHost(), profile.getPort(), true);
            progressBar.step();
        } catch (Exception ex) {
            progressBar.cancel();
            setEnabled(true);
            Log4J.getLogger(LoginDialog.class).error("unable to connect to server", ex);
            JOptionPane.showMessageDialog(this, "Unable to connect to server. Did you misspell the server name?");
            return;
        }
        try {
            if (client.login(profile.getUser(), profile.getPassword()) == false) {
                String result = client.getEvent();
                if (result == null) {
                    result = "Server is not available right now. The server " + "may be down or, if you are using a custom server, " + "you may have entered its name and port number incorrectly.";
                }
                progressBar.cancel();
                setEnabled(true);
                JOptionPane.showMessageDialog(this, result, "Error Logging In", JOptionPane.ERROR_MESSAGE);
            } else {
                progressBar.step();
                progressBar.finish();
                setVisible(false);
                owner.setVisible(false);
                midhedava.doLogin = true;
                client.setUserName(profile.getUser());
            }
        } catch (ariannexpTimeoutException ex) {
            progressBar.cancel();
            setEnabled(true);
            JOptionPane.showMessageDialog(this, "Server does not respond. The server may be down or, " + "if you are using a custom server, you may have entered " + "its name and port number incorrectly.", "Error Logging In", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            progressBar.cancel();
            setEnabled(true);
            JOptionPane.showMessageDialog(this, "Midhedava cannot connect. Please check that your connection " + "is set up and active, then try again.", "Error Logging In", JOptionPane.ERROR_MESSAGE);
        }
    }
