    public Administrator(String protocol, String host, int port, String user, String pass) {
        super("Fedora Administrator");
        INSTANCE = this;
        WATCH_AREA = new JTextArea();
        WATCH_AREA.setFont(new Font("monospaced", Font.PLAIN, 12));
        WATCH_AREA.setCaretPosition(0);
        s_maxButtonHeight = new JTextField("test").getPreferredSize().height;
        BACKGROUND_COLOR = new JPanel().getBackground();
        if (host != null) {
            try {
                String baseURL = protocol + "://" + host + ":" + port + "/fedora";
                FedoraClient fc = new FedoraClient(baseURL, user, pass);
                APIA = fc.getAPIA();
                APIM = fc.getAPIM();
                setLoginInfo(protocol, host, port, user, pass);
            } catch (Exception e) {
                APIA = null;
                APIM = null;
            }
        }
        if (Constants.FEDORA_HOME != null) {
            File f = new File(Constants.FEDORA_HOME);
            if (f.exists() && f.isDirectory()) {
                BASE_DIR = new File(f, "client");
                s_lastDir = BASE_DIR;
            }
        }
        cl = this.getClass().getClassLoader();
        m_aboutPic = new JLabel(new ImageIcon(cl.getResource("images/fedora/aboutadmin.gif")));
        m_aboutText = new JLabel("<html><p>Copyright 2002-2007, The Rector and Visitors of the</p>" + "<p>University of Virginia and Cornell University. All rights reserved.</p><p></p>" + "<p><b>License and Copyright: </b>This software is subject to the</p>" + "<p>Educational Community License (the \"License\"); you may not use</p>" + "<p>this software except in compliance with the License. You may</p>" + "<p>obtain a copy of the License at:</p>" + "<blockquote>http://www.opensource.org/licenses/ecl1.txt.</blockquote><p></p>" + "<p>Software distributed under the License is distributed on an \"AS IS\"</p>" + "<p>basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.</p>" + "<p>See the License for the specific language governing rights and</p>" + "<p>limitations under the License.</p><p></p>" + "<p>Version: " + VERSION + "</p>" + "<p>Release Date: " + RELEASE_DATE + "</p>" + "<p>See http://www.fedora.info/ for more information.</p></html>");
        m_aboutText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel splashPicAndText = new JPanel();
        splashPicAndText.setLayout(new BorderLayout());
        splashPicAndText.setBorder(BorderFactory.createLineBorder(Color.black, 5));
        splashPicAndText.add(m_aboutPic, BorderLayout.CENTER);
        splashPicAndText.add(m_aboutText, BorderLayout.SOUTH);
        JWindow splashScreen = new JWindow();
        splashScreen.getContentPane().add(splashPicAndText);
        splashScreen.pack();
        int xSize = splashScreen.getWidth();
        int ySize = splashScreen.getHeight();
        Dimension screenSize = getToolkit().getScreenSize();
        int xLoc = (screenSize.width / 2) - (xSize / 2);
        int yLoc = (screenSize.height / 2) - (ySize / 2);
        splashScreen.setBounds(xLoc, yLoc, xSize, ySize);
        splashScreen.setVisible(true);
        setIconImage(new ImageIcon(cl.getResource("images/fedora/fedora-icon16.gif")).getImage());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        s_desktop = new MDIDesktopPane();
        s_desktop.setBackground(DESKTOP_COLOR);
        s_desktop.setVisible(true);
        mainPanel.add(new JScrollPane(s_desktop), BorderLayout.CENTER);
        PROGRESS = new JProgressBar(0, 2000);
        PROGRESS.setValue(0);
        PROGRESS.setStringPainted(true);
        PROGRESS.setString("");
        mainPanel.add(PROGRESS, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
        setJMenuBar(createMenuBar());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        splashScreen.setVisible(false);
        s_instance = this;
        int xs = 850;
        int ys = 655;
        Dimension sz = this.getToolkit().getScreenSize();
        int xl = (sz.width / 2) - (xs / 2);
        int yl = (sz.height / 2) - (ys / 2);
        setBounds(xl, yl, xs, ys);
        setVisible(true);
        if (APIA == null || APIM == null) {
            new LoginDialog();
        }
        if (APIA == null || APIM == null) {
            dispose();
            System.exit(0);
        }
    }
