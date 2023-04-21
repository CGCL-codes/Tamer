    public LoginPanel(XMPPJavaClientFrame mfrm) {
        this.setLayout(new GridLayout(0, 1));
        mainFrame = mfrm;
        JLabel lbUserName = new JLabel("user name:");
        txtUserName = new JTextField("fiary", 10);
        JPanel panelUserName = new JPanel();
        panelUserName.add(lbUserName);
        panelUserName.add(txtUserName);
        JLabel lbPsw = new JLabel("pass word:");
        txtPsw = new JTextField("521125", 10);
        JPanel panelPsw = new JPanel();
        panelPsw.add(lbPsw);
        panelPsw.add(txtPsw);
        JLabel lbServer = new JLabel("server:");
        txtServer = new JTextField("eli261.gicp.net", 15);
        JPanel panelServer = new JPanel();
        panelServer.add(lbServer);
        panelServer.add(txtServer);
        JButton btLogin = new JButton("Login");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btLogin);
        add(panelUserName);
        add(panelPsw);
        add(panelServer);
        add(panelButtons);
        btLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String username = txtUserName.getText();
                String psw = txtPsw.getText();
                String server = txtServer.getText();
                if (TheGlobal.connection != null && TheGlobal.connection.isConnected()) {
                    TheGlobal.connection.disconnect();
                }
                TheGlobal.connection = new XMPPConnection(server);
                try {
                    TheGlobal.connection.connect();
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Can not connect to the server");
                    return;
                }
                try {
                    TheGlobal.connection.login(username, psw);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Wrong user name or password.");
                    return;
                }
                if (mainFrame != null) {
                    mainFrame.LoginOk();
                }
            }
        });
    }
