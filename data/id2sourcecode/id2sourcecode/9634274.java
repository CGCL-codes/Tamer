            public void actionPerformed(ActionEvent evt) {
                if (!clientInterface.connect(serverInput.getText())) {
                    System.out.println("Unable to connect");
                    clientInterface.quit();
                    System.exit(0);
                }
                String passW = "";
                for (int i = 0; i < passInput.getPassword().length; i++) {
                    passW = passW + passInput.getPassword()[i];
                }
                if (!clientInterface.login(userInput.getText(), passW)) {
                    final JFrame invLogin = new JFrame("Invalid Login");
                    JButton close = new JButton("Okay");
                    JLabel invLabel = new JLabel("Invalid username or password");
                    JPanel top = new JPanel();
                    JPanel bottom = new JPanel();
                    invLogin.setResizable(false);
                    top.add(invLabel);
                    bottom.add(close);
                    Container contentPane = new Container();
                    contentPane = invLogin.getContentPane();
                    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                    invLogin.setBounds(300, 200, 200, 100);
                    contentPane.add(top);
                    contentPane.add(bottom);
                    invLogin.pack();
                    close.getRootPane().setDefaultButton(close);
                    close.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            invLogin.dispose();
                        }
                    });
                    invLogin.addWindowFocusListener(new WindowFocusListener() {

                        public void windowLostFocus(WindowEvent evt) {
                            invLogin.requestFocus();
                        }

                        public void windowGainedFocus(WindowEvent evt) {
                        }
                    });
                    invLogin.setVisible(true);
                    passInput.setText("");
                    clientInterface.quit();
                } else {
                    loggedIn = true;
                    doLogin.dispose();
                }
            }
