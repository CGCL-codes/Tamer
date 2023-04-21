    public void setup(LogReader log, Progress progress) {
        int settingsDistance = 5;
        this.log = log;
        this.views = new ArrayList<StreamView>();
        try {
            clusters = new ClusterSet(log, progress);
            progress.setNote("Setting up UI...");
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel enclosure = new JPanel();
        enclosure.setOpaque(true);
        enclosure.setBackground(new Color(0, 0, 0));
        enclosure.setLayout(new BoxLayout(enclosure, BoxLayout.Y_AXIS));
        enclosure.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (int i = 0; i < log.numberOfInstances(); i++) {
            StreamView view = new StreamView(log.getInstance(i), this);
            view.setAlignmentX(LEFT_ALIGNMENT);
            views.add(view);
            enclosure.add(view);
            enclosure.add(Box.createVerticalStrut(5));
        }
        JScrollPane scrollPane = new JScrollPane(enclosure);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getHorizontalScrollBar().setUI(new SlickerScrollBarUI(scrollPane.getHorizontalScrollBar(), Color.BLACK, new Color(120, 120, 120), new Color(50, 50, 50), 3, 12));
        scrollPane.getVerticalScrollBar().setUI(new SlickerScrollBarUI(scrollPane.getVerticalScrollBar(), Color.BLACK, new Color(120, 120, 120), new Color(50, 50, 50), 3, 12));
        scrollPane.getHorizontalScrollBar().setBlockIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(10);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        RoundedPanel scrollEnclosure = new RoundedPanel(15, 5, 0);
        scrollEnclosure.setBackground(Color.BLACK);
        scrollEnclosure.setLayout(new BorderLayout());
        scrollEnclosure.add(scrollPane, BorderLayout.CENTER);
        final JSlider hSlider = new JSlider();
        hSlider.setOrientation(JSlider.HORIZONTAL);
        hSlider.setMinimum(1);
        hSlider.setMaximum(1000);
        hSlider.setValue(300);
        hSlider.setUI(new SlickerSliderUI(hSlider));
        hSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                float zoom = (float) hSlider.getValue() / 100f;
                for (StreamView view : views) {
                    view.setHorizontalZoom(zoom);
                }
            }
        });
        final JSlider vSlider = new JSlider();
        vSlider.setOrientation(JSlider.HORIZONTAL);
        vSlider.setMinimum(1);
        vSlider.setMaximum(1000);
        vSlider.setValue(100);
        vSlider.setUI(new SlickerSliderUI(vSlider));
        vSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                float zoom = (float) vSlider.getValue() / 100f;
                for (StreamView view : views) {
                    view.setVerticalZoom(zoom);
                }
            }
        });
        final JSlider fSlider = new JSlider();
        fSlider.setOrientation(JSlider.HORIZONTAL);
        fSlider.setMinimum(0);
        fSlider.setMaximum(800);
        fSlider.setValue(200);
        fSlider.setUI(new SlickerSliderUI(fSlider));
        fSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                float fuzz = (float) fSlider.getValue() / 100f;
                for (StreamView view : views) {
                    view.setFuzziness(fuzz);
                }
            }
        });
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setMinimumSize(new Dimension(200, 100));
        rightPanel.setMaximumSize(new Dimension(200, 1000));
        rightPanel.setPreferredSize(new Dimension(200, 500));
        RoundedPanel adjustPanel = new RoundedPanel(10);
        adjustPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        adjustPanel.setLayout(new BoxLayout(adjustPanel, BoxLayout.Y_AXIS));
        adjustPanel.setBackground(panelBackgroundColor);
        JLabel adjustTitle = new JLabel("View");
        Font bigFong = adjustTitle.getFont().deriveFont(13f);
        adjustTitle.setFont(bigFong);
        adjustTitle.setOpaque(false);
        adjustTitle.setForeground(labelColor);
        adjustTitle.setAlignmentX(LEFT_ALIGNMENT);
        adjustPanel.add(packLeftAligned(adjustTitle));
        adjustPanel.add(Box.createVerticalStrut(settingsDistance));
        JLabel hLabel = new JLabel("Horizontal Zoom");
        Font labelFont = hLabel.getFont().deriveFont(10f);
        hLabel.setFont(labelFont);
        hLabel.setOpaque(false);
        hLabel.setForeground(labelColor);
        hLabel.setAlignmentX(LEFT_ALIGNMENT);
        adjustPanel.add(packLeftAligned(hLabel));
        adjustPanel.add(hSlider);
        adjustPanel.add(Box.createVerticalStrut(settingsDistance));
        JLabel vLabel = new JLabel("Vertical Zoom");
        vLabel.setFont(labelFont);
        vLabel.setOpaque(false);
        vLabel.setForeground(labelColor);
        vLabel.setAlignmentX(LEFT_ALIGNMENT);
        adjustPanel.add(packLeftAligned(vLabel));
        adjustPanel.add(vSlider);
        adjustPanel.add(Box.createVerticalStrut(settingsDistance));
        JLabel fLabel = new JLabel("Fuzziness");
        fLabel.setAlignmentX(LEFT_ALIGNMENT);
        fLabel.setFont(labelFont);
        fLabel.setOpaque(false);
        fLabel.setForeground(labelColor);
        adjustPanel.add(packLeftAligned(fLabel));
        adjustPanel.add(fSlider);
        RoundedPanel abstractionPanel = new RoundedPanel(10);
        abstractionPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        abstractionPanel.setLayout(new BoxLayout(abstractionPanel, BoxLayout.Y_AXIS));
        abstractionPanel.setBackground(panelBackgroundColor);
        JLabel abstractionTitle = new JLabel("Abstraction");
        abstractionTitle.setFont(bigFong);
        abstractionTitle.setOpaque(false);
        abstractionTitle.setForeground(labelColor);
        abstractionTitle.setAlignmentX(LEFT_ALIGNMENT);
        abstractionPanel.add(packLeftAligned(abstractionTitle));
        abstractionPanel.add(Box.createVerticalStrut(settingsDistance));
        JLabel levelLabel = new JLabel("Abstraction Level");
        levelLabel.setFont(labelFont);
        levelLabel.setOpaque(false);
        levelLabel.setForeground(labelColor);
        levelLabel.setAlignmentX(LEFT_ALIGNMENT);
        final JLabel levelValueLabel = new JLabel("Level 0: " + clusters.getOrderedEventClassTable().size() + " entities (0 clusters)");
        levelValueLabel.setFont(levelValueLabel.getFont().deriveFont(10f));
        levelValueLabel.setOpaque(false);
        levelValueLabel.setForeground(labelColor);
        levelValueLabel.setAlignmentX(RIGHT_ALIGNMENT);
        levelSlider = new JSlider();
        levelSlider.setOrientation(JSlider.HORIZONTAL);
        levelSlider.setMinimum(0);
        levelSlider.setMaximum(clusters.getNumberOfLevels());
        levelSlider.setValue(0);
        levelSlider.setUI(new SlickerSliderUI(levelSlider));
        levelSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int level = levelSlider.getValue();
                List<Node> levelNodes = clusters.getOrderedNodesForLevel(level);
                int clusters = 0;
                for (Node node : levelNodes) {
                    if (node instanceof ClusterNode) {
                        clusters++;
                    }
                }
                levelValueLabel.setText("Level " + level + ": " + levelNodes.size() + " entities (" + clusters + " clusters)");
                levelValueLabel.revalidate();
                for (StreamView view : views) {
                    view.setLevel(level, levelNodes);
                }
            }
        });
        abstractionPanel.add(packLeftAligned(levelLabel));
        abstractionPanel.add(levelSlider);
        abstractionPanel.add(packLeftAligned(levelValueLabel));
        RoundedPanel playPanel = new RoundedPanel(10);
        playPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.Y_AXIS));
        playPanel.setBackground(panelBackgroundColor);
        JLabel playTitle = new JLabel("Sound Mapping");
        playTitle.setFont(bigFong);
        playTitle.setOpaque(false);
        playTitle.setForeground(labelColor);
        playTitle.setAlignmentX(LEFT_ALIGNMENT);
        String[] instanceNames = new String[log.numberOfInstances()];
        for (int i = 0; i < log.numberOfInstances(); i++) {
            instanceNames[i] = log.getInstance(i).getName();
        }
        instanceBox = new JComboBox(instanceNames);
        instanceBox.setMaximumSize(new Dimension(160, 25));
        instanceBox.setUI(new SlickerComboBoxUI());
        playButton = new SlickerButton("play");
        playButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (playThread != null) {
                    playThread.stopPlaying();
                } else {
                    playThread = new PlayThread(instanceBox.getSelectedIndex());
                    playThread.start();
                    playButton.setText("stop");
                }
            }
        });
        try {
            synth.open();
        } catch (MidiUnavailableException e1) {
            e1.printStackTrace();
        }
        Instrument[] instruments = synth.getAvailableInstruments();
        final JComboBox instrumentBox = new JComboBox(instruments);
        instrumentBox.setUI(new SlickerComboBoxUI());
        instrumentBox.setMinimumSize(new Dimension(160, 25));
        instrumentBox.setMaximumSize(new Dimension(160, 25));
        instrumentBox.setPreferredSize(new Dimension(160, 25));
        instrumentBox.setSize(new Dimension(160, 25));
        instrumentBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Instrument instr = (Instrument) instrumentBox.getSelectedItem();
                synth.loadInstrument(instr);
                Patch patch = instr.getPatch();
                synth.getChannels()[4].programChange(patch.getBank(), patch.getProgram());
            }
        });
        playPanel.add(packLeftAligned(playTitle));
        playPanel.add(Box.createVerticalStrut(settingsDistance));
        playPanel.add(packLeftAligned(instanceBox));
        playPanel.add(Box.createVerticalStrut(settingsDistance));
        playPanel.add(packLeftAligned(instrumentBox));
        playPanel.add(Box.createVerticalStrut(settingsDistance));
        playPanel.add(packLeftAligned(playButton));
        rightPanel.add(adjustPanel);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(abstractionPanel);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(playPanel);
        rightPanel.add(Box.createVerticalGlue());
        JLabel infoLabel = new JLabel();
        infoLabel.setOpaque(false);
        infoLabel.setForeground(new Color(140, 140, 140));
        infoLabel.setFont(infoLabel.getFont().deriveFont(10f));
        infoLabel.setText("Analyzing '" + log.getFile().getShortName() + "': " + log.getLogSummary().getNumberOfAuditTrailEntries() + " events, from " + log.getLogSummary().getModelElements().length + " classes, in " + log.numberOfInstances() + " traces.");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 3));
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollEnclosure, BorderLayout.CENTER);
        mainPanel.add(packLeftAligned(infoLabel), BorderLayout.SOUTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
    }
