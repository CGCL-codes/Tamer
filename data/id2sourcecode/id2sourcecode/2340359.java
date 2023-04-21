    public SystemWindow(String drv, String url, String uid, String pwd) {
        super(UIUtilities.getTitle(null));
        UserSession.load();
        builder = new ViewBuilder(this);
        command = new ViewCommand(this);
        history = new ViewHistory(this);
        report = new ViewBuildReport(this);
        toolbar = new SystemToolbar(this);
        container = new JPanel(card = new CardLayout());
        container.add("window.builder", builder);
        container.add("window.command", command);
        container.add("window.history", history);
        container.add("window.report", report);
        getContentPane().add(container, BorderLayout.CENTER);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        if (drv != null && url != null) openConnection(drv, url, uid, pwd);
        setVisible(true);
        WindowListener wl = new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                UserSession.save();
            }
        };
    }
