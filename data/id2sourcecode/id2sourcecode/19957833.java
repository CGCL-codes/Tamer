            public void actionPerformed(ActionEvent e) {
                connConnTxt.setForeground(UIManager.getColor("Label.foreground"));
                connConnTxt.setVisible(true);
                connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestStart"));
                connConnProgress.setVisible(true);
                (new Thread() {

                    private ServerSocket server;

                    boolean noOutcome;

                    public void run() {
                        try {
                            int port = Integer.parseInt(connConnPort.getText());
                            final ServerSocket s = Connection.getServer();
                            if (s == null || s.isClosed() || s.getLocalPort() != port) {
                                server = new ServerSocket(port);
                                port = server.getLocalPort();
                                newConnection();
                            } else {
                                port = s.getLocalPort();
                            }
                            if (Util.getDebugLevel() > 30) Util.debug("Port: " + port);
                            if (testPort != port) {
                                if (testPort == Util.getPropInt("connPort")) {
                                    Connection.newPortMappings(-1, port, true);
                                } else {
                                    Connection.newPortMappings(testPort, port, true);
                                }
                            }
                            testPort = port;
                            URL url = new URL(Connection.URL_STR + "?req=portcheck&port=" + port);
                            URLConnection connection = url.openConnection(Connection.getProxy());
                            connection.setRequestProperty("User-Agent", Connection.USER_AGENT);
                            BufferedReader bufferedRdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            noOutcome = true;
                            (new Thread() {

                                public void run() {
                                    try {
                                        Thread.sleep(12000);
                                        if (noOutcome) {
                                            connConnTxt.setForeground(ERROR_COLOR);
                                            connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestCannotConnect"));
                                            connConnProgress.setVisible(false);
                                            server.close();
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }).start();
                            final String line = bufferedRdr.readLine();
                            bufferedRdr.close();
                            if (line != null && line.equals("ok")) {
                                connConnTxt.setForeground(SUCCESS_COLOR);
                                connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestOk"));
                            } else if (line != null && line.equals("later")) {
                                connConnTxt.setForeground(ERROR_COLOR);
                                connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestTryLater"));
                            } else {
                                connConnTxt.setForeground(ERROR_COLOR);
                                connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestWrongResponse"));
                            }
                            connConnProgress.setVisible(false);
                        } catch (java.nio.channels.ClosedChannelException e) {
                            if (Util.getDebugLevel() > 90) e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            connConnTxt.setForeground(ERROR_COLOR);
                            connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestCannotConnect") + "\n" + Util.getMsg("Err_FileNotFound"));
                            connConnProgress.setVisible(false);
                        } catch (BindException e) {
                            connConnTxt.setForeground(ERROR_COLOR);
                            connConnTxt.setText(Util.getMsg("Err_PortInUse") + "!");
                            connConnProgress.setVisible(false);
                        } catch (SocketException e) {
                            connConnTxt.setForeground(ERROR_COLOR);
                            connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestCannotConnect") + "\n" + e.getLocalizedMessage());
                            connConnProgress.setVisible(false);
                        } catch (Exception e) {
                            connConnTxt.setForeground(ERROR_COLOR);
                            connConnTxt.setText(Util.getMsg("Set_Conn_ConnTestCannotConnect") + "\n" + e.toString());
                            connConnProgress.setVisible(false);
                            if (Util.getDebugLevel() > 90) e.printStackTrace();
                        } finally {
                            noOutcome = false;
                            try {
                                if (server != null && !server.isClosed()) server.close();
                            } catch (IOException e) {
                                if (Util.getDebugLevel() > 90) e.printStackTrace();
                            }
                        }
                    }

                    void newConnection() {
                        (new Thread() {

                            public void run() {
                                try {
                                    final Socket socket = server.accept();
                                    newConnection();
                                    socket.setKeepAlive(true);
                                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                    String ln;
                                    while ((ln = in.readLine()) != null) {
                                        if (Util.getDebugLevel() > 30) Util.debug(ln);
                                        if (ln.equals("\0c\0h\0e\0c\0k\0u\0p\0")) {
                                            socket.getOutputStream().write(new byte[] { 0, 'O', 0, 'k' });
                                            socket.close();
                                        } else {
                                            final char c = (ln.length() > 0 ? ln.charAt(0) : 0);
                                            socket.getOutputStream().write(new byte[] { 0, 'U', (byte) (c >> 8), (byte) c });
                                        }
                                    }
                                } catch (SocketException e) {
                                    if (Util.getDebugLevel() > 68) {
                                        if (!e.getMessage().equals("socket closed")) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (IOException e) {
                                    if (Util.getDebugLevel() > 90) e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }).start();
            }
