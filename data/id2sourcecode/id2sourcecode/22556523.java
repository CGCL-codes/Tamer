    public void showModel(final HLPetriNet model) {
        final PetriNetGraph pnGraph = new PetriNetGraph(this, model);
        GradientPanel modelView = new GradientPanel(new Color(60, 60, 60), new Color(90, 90, 90));
        modelView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        modelView.setLayout(new BorderLayout());
        JPanel controlPanel = new RoundedPanel(10, 5, 3);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(new Color(140, 140, 140));
        controlPanel.setMinimumSize(new Dimension(200, 100));
        controlPanel.setMaximumSize(new Dimension(200, 300));
        controlPanel.setPreferredSize(new Dimension(200, 260));
        redesignBox.setUI(new SlickerComboBoxUI());
        redesignBox.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        SlickerButton selectTButton = new SlickerButton("Select transformation");
        selectTButton.setAlignmentX(SlickerButton.LEFT_ALIGNMENT);
        selectTButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                displaySelectedComponents(model, pnGraph);
            }
        });
        SlickerButton selectButton = new SlickerButton("Deselect process part");
        selectButton.setAlignmentX(SlickerButton.LEFT_ALIGNMENT);
        selectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                for (GGNode node : pnGraph.nodes()) {
                    node.setSelected(false);
                }
                for (GGEdge edge : pnGraph.edges()) {
                    edge.setSelected(false);
                }
                displaySelectedComponents(model, pnGraph);
            }
        });
        SlickerButton redesignButton = new SlickerButton("Redesign model");
        redesignButton.setAlignmentX(SlickerButton.LEFT_ALIGNMENT);
        redesignButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                List<Transition> selectedTransitions = pnGraph.getSelectedPetriNetTransitions();
                RedesignType type = (RedesignType) redesignBox.getSelectedItem();
                Component comp = null;
                if (isIncludingAllSelTransitions(type.ordinal(), model, selectedTransitions)) {
                    try {
                        comp = RedesignFactory.getMinimalComponent(type.ordinal(), model, selectedTransitions);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                performRedesign(model, type, comp);
            }
        });
        SlickerButton analysisButton = new SlickerButton("Modify model");
        analysisButton.setAlignmentX(SlickerButton.LEFT_ALIGNMENT);
        analysisButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                EditHighLevelProcessGui gui = new EditHighLevelProcessGui(model);
                ComponentFrame frame = MainUI.getInstance().createAndReturnFrame("View/Edit High Level Process", gui.getVisualization());
                frame.addInternalFrameListener(new InternalFrameListener() {

                    public void internalFrameClosing(InternalFrameEvent e) {
                        HLPetriNet cloned = (HLPetriNet) model.clone();
                        ColoredPetriNet netToExport = new ColoredPetriNet(cloned);
                        CpnExportSettings exportCPN = new CpnExportSettings(myAlgorithm, netToExport, false);
                        exportCPN.saveCPNmodel();
                        String distrLoc = locationForCurrentSimSettings + "\\" + "arrivalRate.sml";
                        FileWriter dout = null;
                        try {
                            dout = new FileWriter(distrLoc);
                            dout.write("fun readArrivalRate() = " + CpnUtils.getCpnDistributionFunction(model.getHLProcess().getGlobalInfo().getCaseGenerationScheme()) + ";");
                            dout.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    public void internalFrameActivated(InternalFrameEvent e) {
                    }

                    public void internalFrameClosed(InternalFrameEvent e) {
                    }

                    public void internalFrameDeiconified(InternalFrameEvent e) {
                    }

                    public void internalFrameIconified(InternalFrameEvent e) {
                    }

                    public void internalFrameOpened(InternalFrameEvent e) {
                    }

                    public void internalFrameDeactivated(InternalFrameEvent e) {
                    }
                });
            }
        });
        JLabel title = new JLabel("Redesign actions");
        title.setOpaque(false);
        title.setFont(title.getFont().deriveFont(14f));
        JLabel title2 = new JLabel("Refinement actions");
        title2.setOpaque(false);
        title2.setFont(title2.getFont().deriveFont(14f));
        controlPanel.add(title);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(selectTButton);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(redesignBox);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(selectButton);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(redesignButton);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(title2);
        controlPanel.add(Box.createVerticalStrut(8));
        controlPanel.add(analysisButton);
        final GGGraphView graphView = new GGGraphView();
        graphView.setGraphSelectionModel(new GGToggleGraphSelectionModel());
        modelView.add(graphView, BorderLayout.CENTER);
        modelView.add(controlPanel, BorderLayout.WEST);
        switchView(modelView);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                graphView.setGraph(pnGraph);
            }
        });
    }
