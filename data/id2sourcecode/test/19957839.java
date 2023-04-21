            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        connProxyTestTxt.setForeground(UIManager.getColor("Label.foreground"));
                        connProxyTestTxt.setVisible(true);
                        connProxyTestTxt.setText(Util.getMsg("Set_Conn_ProxyTestStart"));
                        connProxyProgress.setVisible(true);
                    }
                });
                (new Thread() {

                    public void run() {
                        try {
                            URL url = new URL(Connection.URL_STR + "?req=check");
                            URLConnection connection = url.openConnection(Connection.getProxy(getAsInt(connProxyType), connProxyAddress.getText(), Integer.parseInt(connProxyPort.getText())));
                            connection.setRequestProperty("User-Agent", Connection.USER_AGENT);
                            BufferedReader bufferedRdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            final String line = bufferedRdr.readLine();
                            bufferedRdr.close();
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    if (line.equals("ok")) {
                                        connProxyTestTxt.setForeground(SUCCESS_COLOR);
                                        connProxyTestTxt.setText(Util.getMsg("Set_Conn_ProxyTestOk"));
                                    } else {
                                        connProxyTestTxt.setForeground(ERROR_COLOR);
                                        connProxyTestTxt.setText(Util.getMsg("Set_Conn_ProxyTestWrongResponse"));
                                    }
                                    connProxyProgress.setVisible(false);
                                }
                            });
                        } catch (final FileNotFoundException e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    connProxyTestTxt.setForeground(ERROR_COLOR);
                                    connProxyTestTxt.setText(Util.getMsg("Set_Conn_ProxyTestCannotConnect") + "\n" + Util.getMsg("Set_Conn_ProxyTestFileNotFound"));
                                    connProxyProgress.setVisible(false);
                                }
                            });
                        } catch (final SocketException e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    connProxyTestTxt.setForeground(ERROR_COLOR);
                                    connProxyTestTxt.setText(Util.getMsg("Set_Conn_ProxyTestCannotConnect") + "\n" + e.getLocalizedMessage());
                                    connProxyProgress.setVisible(false);
                                }
                            });
                        } catch (final Exception e) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    connProxyTestTxt.setForeground(ERROR_COLOR);
                                    connProxyTestTxt.setText(Util.getMsg("Set_Conn_ProxyTestCannotConnect") + "\n" + e.toString());
                                    connProxyProgress.setVisible(false);
                                }
                            });
                            if (Util.getDebugLevel() > 90) e.printStackTrace();
                        }
                    }
                }).start();
            }
