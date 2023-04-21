    DialogSettings(JFrame frame) {
        super(frame, "Set_Settings", "settings", false, true, new Runnable() {

            public void run() {
                if (instance.bnOk.isEnabled() && instance.isVisible()) {
                    instance.save();
                    instance.close();
                }
            }
        });
        setMinimumSize(new Dimension(300, 300));
        DocumentListener documentListener = new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                checkErrorsAndChanges();
            }

            public void removeUpdate(DocumentEvent e) {
                checkErrorsAndChanges();
            }

            public void insertUpdate(DocumentEvent e) {
                checkErrorsAndChanges();
            }
        };
        ChangeListener changeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                checkErrorsAndChanges();
            }
        };
        ItemListener itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                checkErrorsAndChanges();
            }
        };
        JPanel panel;
        JPanel spanel;
        JButton button;
        String cache;
        Container contentPane = getContentPane();
        tabbedPane = new JTabbedPane();
        tabbedPane.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                if (gameDevList != null) {
                    gameDevList.setPreferredSize(new Dimension(0, 0));
                    final Dimension subPanel = gameDevPanel.getPreferredSize();
                    final Dimension panel = getSize();
                    subPanel.width = Math.max(Math.max(panel.width - 100, 200), subPanel.width - 50);
                    subPanel.height = Math.max(panel.height - 550, 100);
                    gameDevList.setPreferredSize(subPanel);
                    gameDevList.updateUI();
                }
            }
        });
        tabbedPane.addTab(Util.getMsgMnemonic("Set_Game"), null, new JScrollPane(panel = new JPanel()), Util.getMsg("Set_Game_Help"));
        tabbedPane.setMnemonicAt(GAME, Util.getLastMnemonic());
        tabbedPane.setDisplayedMnemonicIndexAt(GAME, Util.getLastMnemonicIndex());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setName("Set_Game");
        panel.add(spanel = new JPanel());
        spanel.add(spanel = new JPanel());
        spanel.setBorder(new NamedTitledBorder("Set_Game_Gen"));
        spanel.setLayout(Util.GRIDBAG);
        Util.addLabeledComponent(spanel, "Set_Game_GenName", gameGenName = new JTextField(15), documentListener);
        Util.addLabeledComponent(spanel, "Set_Game_GenAskName", gameGenAskName = new JCheckBox(), changeListener);
        Util.addLabeledComponent(spanel, "Set_Game_GenAutoPause", gameGenAutoPause = new JCheckBox(), changeListener);
        gameGenPerformance = new JComboBox();
        for (Map.Entry<Integer, String> entry : Game.PERFORMANCES.entrySet()) {
            gameGenPerformance.addItem(new Item<Integer, String>(entry.getKey(), Util.getMsg(entry.getValue()), entry.getValue()));
        }
        Util.addLabeledComponent(spanel, "Set_Game_GenPerformance", gameGenPerformance, itemListener);
        panel.add(spanel = new JPanel());
        gameDevPanel = spanel;
        spanel.add(spanel = new JPanel());
        spanel.setBorder(new NamedTitledBorder("Set_Game_Dev"));
        spanel.setLayout(Util.GRIDBAG);
        Util.addToCenter(spanel, gameDevList = new DeviceList("deviceList", changeListener));
        Util.addToRight(spanel, button = new JButton());
        Util.updateButtonText(button, "Refresh");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DeviceList.refresh();
            }
        });
        Util.addToCenter(spanel, gameDevTxt = new JTextArea());
        gameDevTxt.setEditable(false);
        gameDevTxt.setBackground(new Color(UIManager.getColor("Panel.background").getRGB()));
        gameDevTxt.setHighlighter(null);
        gameDevTxt.setBorder(new EmptyBorder(10, 10, 10, 10));
        gameDevTxt.setFont(new Font(Font.SANS_SERIF, 0, 12));
        gameDevTxt.setVisible(false);
        Util.addToCenter(spanel, gameDevProgress = new JProgressBar());
        gameDevProgress.setIndeterminate(true);
        gameDevProgress.setVisible(false);
        gameDevPause = new JComboBox();
        final byte max = (byte) MidiDevicer.CONTROLLERS.length;
        for (byte i = 0; i < max; ++i) {
            gameDevPause.addItem(new Item<Byte, String>(MidiDevicer.CONTROLLERS[i], String.format("0x%02x (%d)", MidiDevicer.CONTROLLERS[i], MidiDevicer.CONTROLLERS[i])));
        }
        Util.addLabeledComponent(spanel, "Set_Game_DevPause", gameDevPause, itemListener, button = new JButton());
        Util.updateButtonText(button, "Detect");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        gameDevTxt.setForeground(UIManager.getColor("Label.foreground"));
                        gameDevTxt.setVisible(true);
                        gameDevTxt.setText(Util.getMsg("Set_Game_DevDetectStart"));
                        gameDevProgress.setVisible(true);
                    }
                });
                SEQUENCER.setWaitingForController(true);
                (new Thread() {

                    public void run() {
                        try {
                            Thread.sleep(10100);
                            if (SEQUENCER.isWaitingForController()) {
                                gameDevTxt.setForeground(ERROR_COLOR);
                                gameDevTxt.setText(Util.getMsg("Set_DetectTimeOut"));
                                gameDevProgress.setVisible(false);
                                SEQUENCER.setWaitingForController(false);
                            }
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        });
        tabbedPane.addTab(Util.getMsgMnemonic("Set_View"), null, new JScrollPane(panel = new JPanel()), Util.getMsg("Set_View_Help"));
        tabbedPane.setMnemonicAt(VIEW, Util.getLastMnemonic());
        tabbedPane.setDisplayedMnemonicIndexAt(VIEW, Util.getLastMnemonicIndex());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setName("Set_View");
        panel.add(spanel = new JPanel());
        spanel.add(spanel = new JPanel());
        spanel.setBorder(new NamedTitledBorder("Set_View_Gen"));
        spanel.setLayout(Util.GRIDBAG);
        viewGenNoteLetters = new JComboBox();
        viewGenNoteLetters.addItem(new Item<Integer, String>(0, Util.getMsg("Set_View_GenLetters_None"), "Set_View_GenLetters_None"));
        viewGenNoteLetters.addItem(new Item<Integer, String>(1, Util.getMsg("Set_View_GenLetters_Name"), "Set_View_GenLetters_Name"));
        viewGenNoteLetters.addItem(new Item<Integer, String>(2, Util.getMsg("Set_View_GenLetters_Shorthand"), "Set_View_GenLetters_Shorthand"));
        viewGenNoteLetters.addItem(new Item<Integer, String>(3, Util.getMsg("Set_View_GenLetters_Numbered"), "Set_View_GenLetters_Numbered"));
        Util.addLabeledComponent(spanel, "Set_View_GenNoteLetters", viewGenNoteLetters, itemListener);
        Util.addLabeledComponent(spanel, "Set_View_GenDisplayKeyboard", viewGenKeyboard = new JCheckBox());
        viewGenKeyboard.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                viewGenKeyboardLetters.setEnabled(((JCheckBox) e.getSource()).isSelected());
                checkErrorsAndChanges();
            }
        });
        viewGenKeyboardLetters = new JComboBox();
        viewGenKeyboardLetters.addItem(new Item<Integer, String>(0, Util.getMsg("Set_View_GenLetters_None"), "Set_View_GenLetters_None"));
        viewGenKeyboardLetters.addItem(new Item<Integer, String>(1, Util.getMsg("Set_View_GenLetters_Name"), "Set_View_GenLetters_Name"));
        viewGenKeyboardLetters.addItem(new Item<Integer, String>(2, Util.getMsg("Set_View_GenLetters_Shorthand"), "Set_View_GenLetters_Shorthand"));
        viewGenKeyboardLetters.addItem(new Item<Integer, String>(3, Util.getMsg("Set_View_GenLetters_Numbered"), "Set_View_GenLetters_Numbered"));
        Util.addLabeledComponent(spanel, "Set_View_GenKeyboardLetters", viewGenKeyboardLetters, itemListener);
        viewGenFirstKey = new JComboBox();
        viewGenFirstKey.setToolTipText(cache = Util.getMsg("Set_View_GenKey_Help"));
        viewGenLastKey = new JComboBox();
        viewGenFirstKey.setToolTipText(cache);
        for (byte i = 0; i < 127; ++i) {
            final Key key = new Key(i, 0);
            if (!key.higher) {
                final Item<Byte, String> item;
                viewGenFirstKey.addItem(item = new Item<Byte, String>(i, key.toShorthand() + " | " + key.toNumbered() + " (" + i + ")"));
                viewGenLastKey.addItem(item);
            }
        }
        Util.addLabeledComponent(spanel, "Set_View_GenScoreImages", viewGenScoreImages = new JCheckBox(), changeListener);
        viewGenFirstLabel = Util.addLabeledComponent(spanel, "Set_View_GenFirstKey", viewGenFirstKey, itemListener, button = new JButton());
        Util.updateButtonText(button, "Detect");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        viewGenTxt.setForeground(UIManager.getColor("Label.foreground"));
                        viewGenTxt.setVisible(true);
                        viewGenTxt.setText(Util.getMsg("Set_View_GenDetectFirstStart"));
                        viewGenProgress.setVisible(true);
                    }
                });
                bothKeys = false;
                firstKey = true;
                SEQUENCER.setWaitingForNote(true);
                (new Thread() {

                    private int id = ++detecterId;

                    public void run() {
                        try {
                            Thread.sleep(10100);
                            if (id == detecterId && SEQUENCER.isWaitingForNote()) {
                                viewGenTxt.setForeground(ERROR_COLOR);
                                viewGenTxt.setText(Util.getMsg("Set_DetectTimeOut"));
                                viewGenProgress.setVisible(false);
                                SEQUENCER.setWaitingForNote(false);
                            }
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        });
        viewGenLastLabel = Util.addLabeledComponent(spanel, "Set_View_GenLastKey", viewGenLastKey, itemListener, button = new JButton());
        Util.updateButtonText(button, "AltDetect");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        viewGenTxt.setForeground(UIManager.getColor("Label.foreground"));
                        viewGenTxt.setVisible(true);
                        viewGenTxt.setText(Util.getMsg("Set_View_GenDetectLastStart"));
                        viewGenProgress.setVisible(true);
                    }
                });
                bothKeys = false;
                firstKey = false;
                SEQUENCER.setWaitingForNote(true);
                (new Thread() {

                    private int id = ++detecterId;

                    public void run() {
                        try {
                            Thread.sleep(10100);
                            if (id == detecterId && SEQUENCER.isWaitingForNote()) {
                                viewGenTxt.setForeground(ERROR_COLOR);
                                viewGenTxt.setText(Util.getMsg("Set_DetectTimeOut"));
                                viewGenProgress.setVisible(false);
                                SEQUENCER.setWaitingForNote(false);
                            }
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        });
        Util.addToCenter(spanel, button = new JButton());
        Util.updateButtonText(button, "Set_View_Gen_DetectBothKeys");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        viewGenTxt.setForeground(UIManager.getColor("Label.foreground"));
                        viewGenTxt.setVisible(true);
                        viewGenTxt.setText(Util.getMsg("Set_View_GenDetectFirstStart"));
                        viewGenProgress.setVisible(true);
                    }
                });
                bothKeys = true;
                firstKey = true;
                SEQUENCER.setWaitingForNote(true);
                (new Thread() {

                    private int id = ++detecterId;

                    public void run() {
                        try {
                            Thread.sleep(10100);
                            if (id == detecterId && SEQUENCER.isWaitingForNote()) {
                                viewGenTxt.setForeground(ERROR_COLOR);
                                viewGenTxt.setText(Util.getMsg("Set_DetectTimeOut"));
                                viewGenProgress.setVisible(false);
                                SEQUENCER.setWaitingForNote(false);
                            }
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
        });
        Util.addToCenter(spanel, viewGenTxt = new JTextArea());
        viewGenTxt.setEditable(false);
        viewGenTxt.setBackground(new Color(UIManager.getColor("Panel.background").getRGB()));
        viewGenTxt.setHighlighter(null);
        viewGenTxt.setBorder(new EmptyBorder(10, 10, 10, 10));
        viewGenTxt.setFont(new Font(Font.SANS_SERIF, 0, 12));
        viewGenTxt.setVisible(false);
        Util.addToCenter(spanel, viewGenProgress = new JProgressBar());
        viewGenProgress.setIndeterminate(true);
        viewGenProgress.setVisible(false);
        panel.add(spanel = new JPanel());
        spanel.add(spanel = new JPanel());
        spanel.setBorder(new NamedTitledBorder("Set_View_Full"));
        spanel.setLayout(Util.GRIDBAG);
        Util.addLabeledComponent(spanel, "Set_View_FullDevice", viewFullDevice = new JComboBox());
        Util.addLabeledComponent(spanel, "Set_View_FullResolution", viewFullResolution = new JComboBox());
        Util.addLabeledComponent(spanel, "Set_View_FullColorDepth", viewFullColorDepth = new JComboBox());
        Util.addLabeledComponent(spanel, "Set_View_FullRefreshRate", viewFullRefreshRate = new JComboBox(), itemListener);
        viewFullDevice.addItemListener(new ItemListener() {

            @SuppressWarnings("unchecked")
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    final String device = ((Item<String, String>) e.getItem()).getKey();
                    graphicsDevice = null;
                    GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    if (device == null) {
                        graphicsDevice = genv.getDefaultScreenDevice();
                    } else {
                        for (GraphicsDevice g : genv.getScreenDevices()) {
                            if (device.equals(g.getIDstring())) {
                                graphicsDevice = g;
                                break;
                            }
                        }
                    }
                    if (graphicsDevice != null) {
                        ArrayList<Dimension> array = new ArrayList<Dimension>();
                        Dimension dim;
                        for (DisplayMode d : graphicsDevice.getDisplayModes()) {
                            dim = new Dimension(d.getWidth(), d.getHeight());
                            if (!array.contains(dim)) array.add(dim);
                        }
                        Item<Dimension, String> item = (Item<Dimension, String>) viewFullResolution.getSelectedItem();
                        Dimension selected = null;
                        if (item != null) selected = item.getKey();
                        viewFullResolution.removeAllItems();
                        viewFullResolution.addItem(new Item<Dimension, String>(null, Util.getMsgMnemonic("Default")));
                        for (Dimension d : array) {
                            viewFullResolution.addItem(new Item<Dimension, String>(d, d.width + " × " + d.height));
                            if (d.equals(selected)) viewFullResolution.setSelectedIndex(viewFullResolution.getItemCount() - 1);
                        }
                    }
                }
                checkErrorsAndChanges();
            }
        });
        viewFullResolution.addItemListener(new ItemListener() {

            @SuppressWarnings("unchecked")
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (graphicsDevice != null) {
                        Dimension resolution = ((Item<Dimension, Item>) e.getItem()).getKey();
                        if (resolution == null) {
                            DisplayMode dm = graphicsDevice.getDisplayMode();
                            resolution = new Dimension(dm.getWidth(), dm.getHeight());
                        }
                        ArrayList<Integer> array = new ArrayList<Integer>();
                        Integer i;
                        for (DisplayMode d : graphicsDevice.getDisplayModes()) {
                            if (d.getWidth() == resolution.width && d.getHeight() == resolution.height) {
                                if (!array.contains(i = d.getBitDepth())) array.add(i);
                            }
                        }
                        Item<Integer, String> item = (Item<Integer, String>) viewFullColorDepth.getSelectedItem();
                        Integer selected = null;
                        if (item != null) selected = item.getKey();
                        viewFullColorDepth.removeAllItems();
                        viewFullColorDepth.addItem(new Item<Integer, String>(null, Util.getMsgMnemonic("Default")));
                        for (Integer element : array) {
                            viewFullColorDepth.addItem(new Item<Integer, String>(element, element.toString()));
                            if (element.equals(selected)) viewFullColorDepth.setSelectedIndex(viewFullColorDepth.getItemCount() - 1);
                        }
                    }
                }
                checkErrorsAndChanges();
            }
        });
        viewFullColorDepth.addItemListener(new ItemListener() {

            @SuppressWarnings("unchecked")
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (graphicsDevice != null) {
                        Dimension resolution = ((Item<Dimension, Item>) viewFullResolution.getSelectedItem()).getKey();
                        if (resolution == null) {
                            DisplayMode dm = graphicsDevice.getDisplayMode();
                            resolution = new Dimension(dm.getWidth(), dm.getHeight());
                        }
                        Integer colorDepth = ((Item<Integer, String>) e.getItem()).getKey();
                        if (colorDepth == null) {
                            colorDepth = graphicsDevice.getDisplayMode().getBitDepth();
                        }
                        ArrayList<Integer> array = new ArrayList<Integer>();
                        Integer i;
                        for (DisplayMode d : graphicsDevice.getDisplayModes()) {
                            if (d.getWidth() == resolution.width && d.getHeight() == resolution.height && d.getBitDepth() == colorDepth) {
                                if (!array.contains(i = d.getRefreshRate())) array.add(i);
                            }
                        }
                        Item<Integer, String> item = (Item<Integer, String>) viewFullRefreshRate.getSelectedItem();
                        Integer selected = null;
                        if (item != null) selected = item.getKey();
                        viewFullRefreshRate.removeAllItems();
                        viewFullRefreshRate.addItem(new Item<Integer, String>(null, Util.getMsgMnemonic("Default")));
                        for (Integer element : array) {
                            viewFullRefreshRate.addItem(new Item<Integer, String>(element, element.toString()));
                            if (element.equals(selected)) viewFullRefreshRate.setSelectedIndex(viewFullRefreshRate.getItemCount() - 1);
                        }
                    }
                }
                checkErrorsAndChanges();
            }
        });
        Util.addToCenter(spanel, button = new JButton());
        Util.updateButtonText(button, "Set_View_FullIdentifyDevices");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final GraphicsDevice def = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                identifierFrames.clear();
                final Thread thread = new Thread() {

                    public void run() {
                        try {
                            Thread.sleep(3000);
                            try {
                                for (JFrame frame : identifierFrames) {
                                    frame.setVisible(false);
                                    frame.dispose();
                                }
                            } catch (Exception ex) {
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                };
                for (final GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                    final String id = def.equals(gd) ? gd.getIDstring() + " (" + Util.getMsgMnemonic("Default") + ")" : gd.getIDstring();
                    final JFrame frame = new JFrame(id);
                    final Rectangle bounds = gd.getDefaultConfiguration().getBounds();
                    final TransparentComponent component = new TransparentComponent(frame, bounds);
                    identifierFrames.add(frame);
                    component.setLayout(new OverlayLayout(component));
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.setOpaque(false);
                    component.add(panel);
                    JLabel label = new JLabel(id, 0);
                    label.setFont(new Font(null, Font.BOLD, 96));
                    label.setForeground(Color.LIGHT_GRAY);
                    panel.add(label, BorderLayout.CENTER);
                    panel = new JPanel(new BorderLayout());
                    panel.setOpaque(false);
                    component.add(panel);
                    label = new JLabel(id, 0);
                    label.setBorder(new EmptyBorder(15, 15, 0, 0));
                    label.setFont(new Font(null, Font.BOLD, 96));
                    label.setForeground(Color.BLACK);
                    panel.add(label, BorderLayout.CENTER);
                    frame.setLayout(new BorderLayout());
                    frame.getContentPane().add("Center", component);
                    frame.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent e) {
                            thread.interrupt();
                            for (JFrame frame : identifierFrames) {
                                try {
                                    frame.setVisible(false);
                                    frame.dispose();
                                } catch (Exception ex) {
                                }
                            }
                        }
                    });
                    frame.addKeyListener(new KeyAdapter() {

                        public void keyPressed(KeyEvent e) {
                            switch(e.getKeyCode()) {
                                case KeyEvent.VK_ESCAPE:
                                case KeyEvent.VK_ENTER:
                                case KeyEvent.VK_SPACE:
                                    thread.interrupt();
                                    for (JFrame frame : identifierFrames) {
                                        try {
                                            frame.setVisible(false);
                                            frame.dispose();
                                        } catch (Exception ex) {
                                        }
                                    }
                                    break;
                            }
                        }
                    });
                    frame.setUndecorated(true);
                    frame.setBounds(bounds);
                    frame.setVisible(true);
                }
                thread.start();
            }
        });
        tabbedPane.addTab(Util.getMsgMnemonic("Set_Connection"), null, new JScrollPane(panel = new JPanel()), Util.getMsg("Set_Connection_Help"));
        tabbedPane.setMnemonicAt(CONNECTION, Util.getLastMnemonic());
        tabbedPane.setDisplayedMnemonicIndexAt(CONNECTION, Util.getLastMnemonicIndex());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setName("Set_Connection");
        panel.add(spanel = new JPanel());
        spanel.add(spanel = new JPanel());
        spanel.setBorder(new NamedTitledBorder("Set_Conn_Conn"));
        spanel.setLayout(Util.GRIDBAG);
        Util.addLabeledComponent(spanel, "Set_Conn_ConnUpdate", connConnUpdate = new JCheckBox(), changeListener);
        Util.addLabeledComponent(spanel, "Set_Conn_ConnToplist", connConnToplist = new JCheckBox(), changeListener);
        Util.addLabeledComponent(spanel, "Set_Conn_ConnClients", connConnClients = new JCheckBox());
        connConnPortLabel = Util.addLabeledComponent(spanel, "Set_Conn_ConnPort", connConnPort = new JTextField(5), documentListener);
        connConnPort.setDocument(new NumericDocument());
        connConnPort.getDocument().addDocumentListener(documentListener);
        connConnClients.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                final boolean active = ((JCheckBox) e.getSource()).isSelected();
                connConnPort.setEnabled(active);
                connConnTest.setEnabled(active);
                if (!active) {
                    final int port = Integer.parseInt(connConnPort.getText());
                    if (port < 0) {
                        connConnPort.setText("0");
                    } else if (port > 65535) {
                        connConnPort.setText("65535");
                    }
                }
                checkErrorsAndChanges();
            }
        });
        Util.addToCenter(spanel, connConnTxt = new JTextArea());
        connConnTxt.setEditable(false);
        connConnTxt.setBackground(new Color(UIManager.getColor("Panel.background").getRGB()));
        connConnTxt.setHighlighter(null);
        connConnTxt.setBorder(new EmptyBorder(10, 10, 10, 10));
        connConnTxt.setFont(new Font(Font.SANS_SERIF, 0, 12));
        connConnTxt.setVisible(false);
        Util.addToCenter(spanel, connConnProgress = new JProgressBar());
        connConnProgress.setIndeterminate(true);
        connConnProgress.setVisible(false);
        Util.addToCenter(spanel, connConnTest = new JButton());
        Util.updateButtonText(connConnTest, "Set_Conn_ConnTest");
        connConnTest.addActionListener(new ActionListener() {

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
        });
        panel.add(spanel = new JPanel());
        spanel.add(spanel = new JPanel());
        spanel.setBorder(new NamedTitledBorder("Set_Conn_Proxy"));
        spanel.setLayout(Util.GRIDBAG);
        Util.addLabeledComponent(spanel, "Set_Conn_ProxyType", connProxyType = new JComboBox());
        connProxyType.addItem(new Item<Integer, String>(0, Util.getMsg("Set_Conn_ProxyAuto"), "Set_Conn_ProxyAuto"));
        connProxyType.addItem(new Item<Integer, String>(1, Util.getMsg("Set_Conn_ProxyDirect"), "Set_Conn_ProxyDirect"));
        connProxyType.addItem(new Item<Integer, String>(2, Util.getMsg("Set_Conn_ProxyHttp"), "Set_Conn_ProxyHttp"));
        connProxyType.addItem(new Item<Integer, String>(3, Util.getMsg("Set_Conn_ProxySocks"), "Set_Conn_ProxySocks"));
        Util.addLabeledComponent(spanel, "Set_Conn_ProxyAddress", connProxyAddress = new JTextField(20), documentListener);
        connProxyAddress.getDocument().addDocumentListener(documentListener);
        connProxyPortLabel = Util.addLabeledComponent(spanel, "Set_Conn_ProxyPort", connProxyPort = new JTextField(5), documentListener);
        connProxyPort.setDocument(new NumericDocument());
        connProxyPort.getDocument().addDocumentListener(documentListener);
        Util.addLabeledComponent(spanel, "Set_Conn_ProxyNoLocal", connProxyNoLocal = new JCheckBox(), changeListener);
        connProxyType.addItemListener(new ItemListener() {

            @SuppressWarnings("unchecked")
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    final boolean active = ((Item<Integer, String>) e.getItem()).getKey() >= 2;
                    connProxyAddress.setEnabled(active);
                    connProxyPort.setEnabled(active);
                    if (!active) {
                        final int port = Integer.parseInt(connProxyPort.getText());
                        if (port < 0) {
                            connProxyPort.setText("0");
                        } else if (port > 65535) {
                            connProxyPort.setText("65535");
                        }
                    }
                }
                checkErrorsAndChanges();
            }
        });
        Util.addToCenter(spanel, connProxyTestTxt = new JTextArea());
        connProxyTestTxt.setEditable(false);
        connProxyTestTxt.setBackground(new Color(UIManager.getColor("Panel.background").getRGB()));
        connProxyTestTxt.setHighlighter(null);
        connProxyTestTxt.setBorder(new EmptyBorder(10, 10, 10, 10));
        connProxyTestTxt.setFont(new Font(Font.SANS_SERIF, 0, 12));
        connProxyTestTxt.setVisible(false);
        Util.addToCenter(spanel, connProxyProgress = new JProgressBar());
        connProxyProgress.setIndeterminate(true);
        connProxyProgress.setVisible(false);
        Util.addToCenter(spanel, connProxyTest = new JButton());
        Util.updateButtonText(connProxyTest, "Set_Conn_ProxyTest");
        connProxyTest.addActionListener(new ActionListener() {

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
        });
        contentPane.add("Center", tabbedPane);
        contentPane.add("South", panel = new JPanel());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(spanel = new JPanel());
        spanel.setLayout(new FlowLayout());
        spanel.add(errorArea = new JTextArea());
        errorArea.setEditable(false);
        errorArea.setBackground(new Color(spanel.getBackground().getRGB()));
        errorArea.setHighlighter(null);
        errorArea.setBorder(new EmptyBorder(0, 0, 0, 0));
        errorArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        errorArea.setForeground(ERROR_COLOR);
        panel.add(spanel = new JPanel());
        spanel.setLayout(new FlowLayout());
        spanel.add(bnOk = new JButton());
        Util.updateButtonText(bnOk, "OK");
        bnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save();
                close();
            }
        });
        getRootPane().setDefaultButton(bnOk);
        spanel.add(bnCancel = new JButton());
        Util.updateButtonText(bnCancel, "Cancel");
        bnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        spanel.add(bnApply = new JButton());
        Util.updateButtonText(bnApply, "Apply");
        bnApply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save();
                checkErrorsAndChanges();
            }
        });
        tabbedPane.setSelectedIndex(Util.getPropInt("settingsTabIndex"));
        contentPane.add(tabbedPane);
        refresh();
        load();
        instance = this;
    }
