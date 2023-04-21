    private String login() {
        doLogin = new JFrame();
        JLabel userLabel = new JLabel("username:");
        JLabel passLabel = new JLabel("password:");
        JLabel serverLabel = new JLabel("server:");
        final JTextField userInput = new JTextField(15);
        final JPasswordField passInput = new JPasswordField(15);
        final JTextField serverInput = new JTextField(17);
        doLogin.setResizable(false);
        JButton signIn = new JButton("Sign in");
        JButton cancel = new JButton("Cancel");
        JButton newAcct = new JButton("New Account");
        Container contentPane = doLogin.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        doLogin.setBounds(200, 200, 300, 300);
        JPanel userPanel = new JPanel();
        JPanel passPanel = new JPanel();
        JPanel serverPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        userPanel.add(userLabel);
        userPanel.add(userInput);
        passPanel.add(passLabel);
        passPanel.add(passInput);
        serverPanel.add(serverLabel);
        serverPanel.add(serverInput);
        buttonPanel.add(signIn);
        buttonPanel.add(cancel);
        buttonPanel.add(newAcct);
        doLogin.setTitle("Login");
        contentPane.add(userPanel);
        contentPane.add(passPanel);
        contentPane.add(serverPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 50)));
        contentPane.add(buttonPanel);
        signIn.getRootPane().setDefaultButton(signIn);
        doLogin.pack();
        doLogin.setVisible(true);
        signIn.addActionListener(new ActionListener() {

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
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                clientInterface.quit();
                File cache = new File("clientImages\\");
                recursiveRemoveDir(cache);
                if (!cache.exists()) {
                    cache.mkdir();
                }
                System.exit(0);
            }
        });
        newAcct.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.out.println("New Account added");
            }
        });
        doLogin.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent e) {
                clientInterface.quit();
                File cache = new File("clientImages\\");
                recursiveRemoveDir(cache);
                if (!cache.exists()) {
                    cache.mkdir();
                }
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        while (!loggedIn) ;
        return userInput.getText();
    }
