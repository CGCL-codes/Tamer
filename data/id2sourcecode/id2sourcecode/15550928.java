    public TrackGraphicalViewer() {
        initComponents();
        initActions();
        m_trackPanel = new TrackPanel();
        getContentPane().add(m_trackPanel, java.awt.BorderLayout.CENTER);
    }
