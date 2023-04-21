    public TrackWindow(Track selectedTrack, String trackFileName, MainGUI origContainer) {
        super("Track File: " + trackFileName);
        fileName = trackFileName;
        initComponents();
        setContentPane(trackEditorWindow);
        currentTrack = selectedTrack;
        appFrame = origContainer;
        treeWindow = new TrackTreeSelector(trackEditorWindow, this);
        mapWindow = new TrackGraphicalViewer();
        mapWindow.setTrack(currentTrack);
        treeWindow.setTrack(currentTrack);
        treeWindow.populateTree();
        trackEditorPane.setLeftComponent(treeWindow);
        trackEditorPane.setRightComponent(mapWindow);
        treeWindow.setVisible(true);
        mapWindow.setVisible(true);
        addInternalFrameListener(this);
        closingWindow = false;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
