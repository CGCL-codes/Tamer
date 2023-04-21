        public void run() {
            try {
                int portnum = 0;
                try {
                    portnum = Integer.parseInt(port.getText());
                } catch (NumberFormatException nfe) {
                }
                p.put("HOST", host.getText());
                p.putInt("PORT", portnum);
                p.put("USERNAME", username.getText());
                ClientConnection.getConnection().connect();
                done = ClientConnection.getConnection().login(username.getText(), new String(jpf.getPassword()));
                p.put("PASSWORD", keepPassword.isSelected() ? new String(jpf.getPassword()) : "");
                Properties.setKeepPassword(keepPassword.isSelected());
                Properties.setAutoLogin(autoLogin.isSelected());
                if (!done) {
                    p.flush();
                    p.sync();
                    setStatus("login_wrong");
                    autoLogin.setSelected(false);
                    setVisible(true);
                } else {
                    setVisible(false);
                }
            } catch (SocketException e1) {
                setStatus("login_reset");
            } catch (UnknownHostException e1) {
                setStatus("login_host_unknown");
            } catch (IOException e1) {
                setStatus("login_unable_to_connect");
            } catch (BackingStoreException e1) {
                e1.printStackTrace();
            }
            progress.setIndeterminate(false);
        }
