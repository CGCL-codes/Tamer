        @Override
        public void run() {
            Connection pri = null;
            do {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                }
                if (profile != null) pri = profile.getPrimaryConnection();
                if ((pri != null) && (pri.getMyUser() == null)) pri = null;
            } while (pri == null);
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                bw = new BufferedWriter(new OutputStreamWriter(os));
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((is.available() == 0) && socket.isConnected()) sleep(500);
                int loginAttempts = 0;
                while (!connected && (++loginAttempts < 3)) {
                    if (loginAttempts == 1) {
                        int b = is.read();
                        while (b == 255) {
                            sleep(500);
                            b = is.read();
                        }
                        if ((b != 'c') && (b != 3)) break;
                        Out.debug(TelnetEventHandler.class, "Connection established from " + socket.getRemoteSocketAddress().toString());
                    }
                    sendInternal("Username: ");
                    String username = br.readLine();
                    if (username.length() == 0) username = br.readLine();
                    if (username.length() == 0) continue;
                    while (username.charAt(0) < 0x20) username = username.substring(1);
                    ConnectionSettings cs = pri.getConnectionSettings();
                    if (!username.equalsIgnoreCase(cs.username)) {
                        sendInternal("1019 Error \"Invalid username\"");
                        continue;
                    }
                    sendInternal("Password: ");
                    String password = br.readLine();
                    if (!password.equalsIgnoreCase(cs.password)) {
                        sendInternal("1019 Error \"Invalid password\"");
                        continue;
                    }
                    connected = true;
                }
                if (connected) {
                    Out.debug(TelnetEventHandler.class, "Login accepted from " + socket.getRemoteSocketAddress().toString());
                    dispatch(ChatEvent.NAME, pri.getMyUser().getShortLogonName());
                    dispatch(ChatEvent.CHANNEL, quoteText(pri.getChannel()));
                    for (BNetUser user : pri.getUsers()) dispatchUserDetail(ChatEvent.USER, user);
                } else send("Login failed");
                try {
                    while (connected) {
                        if (!socket.isConnected()) break;
                        pri.sendChatInternal(br.readLine());
                    }
                } catch (SocketException e) {
                    Out.debug(TelnetEventHandler.class, socket.getRemoteSocketAddress().toString() + " " + e.getMessage());
                }
            } catch (Exception e) {
                Out.exception(e);
            }
            Out.debug(TelnetEventHandler.class, "Connection closed from " + socket.getRemoteSocketAddress().toString());
            try {
                socket.close();
            } catch (Exception e) {
            }
            connections.remove(this);
        }
