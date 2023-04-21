    public RecorderDialog(Session doc) throws IOException {
        super(((doc.getFrame() != null) && (doc.getFrame().getWindow() instanceof Frame)) ? (Frame) doc.getFrame().getWindow() : null, AbstractApplication.getApplication().getResourceString("dlgRecorder"), true);
        setResizable(false);
        docFrame = doc.getFrame();
        superCollider = SuperColliderClient.getInstance();
        numChannels = doc.getAudioTrail().getChannelNum();
        doc.getTransport().stop();
        final Application app = AbstractApplication.getApplication();
        final SpringPanel recPane;
        final Container cp = getContentPane();
        final JPanel butPane;
        final WindowAdapter winListener;
        final String className = getClass().getName();
        final AudioFileDescr displayAFD = doc.getDisplayDescr();
        final JButton ggPeakReset;
        final JToolBar tbMonitoring;
        final TimeLabel lbTime;
        final MessageFormat frmtPeak = new MessageFormat(getResourceString("msgPeak"), Locale.US);
        final Object[] peakArgs = new Object[1];
        final JRootPane rp = getRootPane();
        final InputMap imap = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap amap = rp.getActionMap();
        final JButton ggAbort, ggRecord, ggStop, ggClose;
        final int myMeta = BasicMenuFactory.MENU_SHORTCUT == InputEvent.CTRL_MASK ? InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK : BasicMenuFactory.MENU_SHORTCUT;
        encodingString = (displayAFD.sampleFormat == AudioFileDescr.FORMAT_INT ? "int" : "float") + String.valueOf(displayAFD.bitsPerSample);
        audioPrefs = app.getUserPrefs().node(PrefsUtil.NODE_AUDIO);
        classPrefs = app.getUserPrefs().node(className.substring(className.lastIndexOf('.') + 1));
        recPane = new SpringPanel(4, 2, 4, 2);
        ggRecordConfig = new PrefComboBox();
        ggRecordConfig.setFocusable(false);
        lbPeak = new JLabel();
        actionPeakReset = new ActionPeakReset();
        ggPeakReset = new JButton(actionPeakReset);
        ggPeakReset.setFocusable(false);
        lbTime = new TimeLabel();
        tbMonitoring = new JToolBar();
        tbMonitoring.setFloatable(false);
        ggMonitoring = new JToggleButton(new ActionMonitoring());
        ggMonitoring.setFocusable(false);
        tbMonitoring.add(ggMonitoring);
        recPane.gridAdd(lbTime, 1, 0, -2, 1);
        recPane.gridAdd(new JLabel(getResourceString("labelRecInputs"), SwingConstants.RIGHT), 0, 1);
        recPane.gridAdd(ggRecordConfig, 1, 1, -1, 1);
        recPane.gridAdd(tbMonitoring, 2, 1);
        recPane.gridAdd(new JLabel(getResourceString("labelHeadroom") + " :", SwingConstants.RIGHT), 0, 2);
        recPane.gridAdd(lbPeak, 1, 2);
        recPane.gridAdd(ggPeakReset, 2, 2, -1, 1);
        refillConfigs();
        ggRecordConfig.setPreferences(classPrefs, KEY_CONFIG);
        recPane.makeCompactGrid();
        butPane = new JPanel();
        butPane.setLayout(new BoxLayout(butPane, BoxLayout.X_AXIS));
        actionRecord = new ActionRecord();
        actionStop = new ActionStop();
        actionAbort = new ActionAbort();
        actionClose = new ActionClose();
        butPane.add(new HelpButton("RecorderDialog"));
        butPane.add(Box.createHorizontalGlue());
        ggAbort = new JButton(actionAbort);
        ggAbort.setFocusable(false);
        butPane.add(ggAbort);
        ggRecord = new JButton(actionRecord);
        ggRecord.setFocusable(false);
        butPane.add(ggRecord);
        ggStop = new JButton(actionStop);
        ggStop.setFocusable(false);
        butPane.add(ggStop);
        ggClose = new JButton(actionClose);
        ggClose.setFocusable(false);
        butPane.add(ggClose);
        butPane.add(CoverGrowBox.create());
        cp.add(recPane, BorderLayout.NORTH);
        cp.add(butPane, BorderLayout.SOUTH);
        GUIUtil.setDeepFont(cp, app.getGraphicsHandler().getFont(GraphicsHandler.FONT_SYSTEM | GraphicsHandler.FONT_SMALL));
        meterTimer = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final float value = docFrame.getMaxMeterHold();
                final boolean valueClip = value > -0.2f;
                peakArgs[0] = new Float(value);
                lbPeak.setText(frmtPeak.format(peakArgs));
                if (valueClip && !clipped) {
                    clipped = valueClip;
                    lbPeak.setForeground(Color.red);
                }
            }
        });
        recLenTimer = new RecLenTimer(lbTime, recFrames, doc.timeline.getRate());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        server = superCollider.getServer();
        player = superCollider.getPlayerForDocument(doc);
        if ((server == null) || (player == null) || !server.isRunning()) {
            throw new IOException(getResourceString("errServerNotRunning"));
        }
        osc = new OSCRouterWrapper(doc, this);
        winListener = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (!isRecording) {
                    disposeRecorder();
                }
            }
        };
        addWindowListener(winListener);
        superCollider.addServerListener(this);
        nw = superCollider.getNodeWatcher();
        nw.addListener(this);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, myMeta), "record");
        amap.put("record", new DoClickAction(ggRecord));
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, myMeta), "abort");
        amap.put("abort", new DoClickAction(ggAbort));
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, myMeta), "stop");
        amap.put("stop", new DoClickAction(ggStop));
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        amap.put("close", actionClose);
        docFrame.setForceMeters(true);
        player.setActiveInput(true);
        meterTimer.start();
        GUIUtil.setInitialDialogFocus(rp);
        new DynamicAncestorAdapter(new DynamicPrefChangeManager(classPrefs, new String[] { KEY_CONFIG }, this)).addTo(getRootPane());
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
