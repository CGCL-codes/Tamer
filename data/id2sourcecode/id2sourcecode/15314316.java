    private void initComponents(List<String> channels, List<String> fileFormats) {
        channelModel = new DefaultListModel();
        for (int i = 0; i < channels.size(); i++) {
            String channelName = (String) channels.get(i);
            Channel channel = RBNBController.getInstance().getChannel(channelName);
            String mime = channel.getMetadata("mime");
            if (mime.equals("application/octet-stream")) {
                channelModel.addElement(new ExportChannel(channelName));
            }
        }
        JPanel container = new JPanel();
        setContentPane(container);
        InputMap inputMap = container.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = container.getActionMap();
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.ipadx = 0;
        c.ipady = 0;
        JLabel headerLabel = new JLabel("Select the time range and data channels to export.");
        headerLabel.setBackground(Color.white);
        headerLabel.setOpaque(true);
        headerLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new java.awt.Insets(0, 0, 0, 0);
        container.add(headerLabel, c);
        JPanel timeButtonPanel = new JPanel();
        timeButtonPanel.setLayout(new BorderLayout());
        MouseListener hoverMouseListener = new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                e.getComponent().setForeground(Color.red);
            }

            public void mouseExited(MouseEvent e) {
                e.getComponent().setForeground(Color.blue);
            }
        };
        startTimeButton = new JButton();
        startTimeButton.setBorder(null);
        startTimeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startTimeButton.setForeground(Color.blue);
        startTimeButton.addMouseListener(hoverMouseListener);
        startTimeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                double startTime = DateTimeDialog.showDialog(ExportDialog.this, timeSlider.getStart(), timeSlider.getMinimum(), timeSlider.getEnd());
                if (startTime >= 0) {
                    timeSlider.setStart(startTime);
                }
            }
        });
        timeButtonPanel.add(startTimeButton, BorderLayout.WEST);
        durationLabel = new JLabel();
        durationLabel.setHorizontalAlignment(JLabel.CENTER);
        timeButtonPanel.add(durationLabel, BorderLayout.CENTER);
        endTimeButton = new JButton();
        endTimeButton.setBorder(null);
        endTimeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        endTimeButton.setForeground(Color.blue);
        endTimeButton.addMouseListener(hoverMouseListener);
        endTimeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                double endTime = DateTimeDialog.showDialog(ExportDialog.this, timeSlider.getEnd(), timeSlider.getStart(), timeSlider.getMaximum());
                if (endTime >= 0) {
                    timeSlider.setEnd(endTime);
                }
            }
        });
        timeButtonPanel.add(endTimeButton, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new java.awt.Insets(10, 10, 10, 10);
        container.add(timeButtonPanel, c);
        timeSlider = new TimeSlider();
        timeSlider.setValueChangeable(false);
        timeSlider.setValueVisible(false);
        timeSlider.addTimeAdjustmentListener(new TimeAdjustmentListener() {

            public void timeChanged(TimeEvent event) {
            }

            public void rangeChanged(TimeEvent event) {
                updateTimeRangeLabel();
            }

            public void boundsChanged(TimeEvent event) {
            }
        });
        updateTimeRangeLabel();
        updateTimeBounds();
        List<EventMarker> markers = RBNBController.getInstance().getMarkerManager().getMarkers();
        for (EventMarker marker : markers) {
            timeSlider.addMarker(marker);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new java.awt.Insets(0, 10, 10, 10);
        container.add(timeSlider, c);
        JLabel numericHeaderLabel = new JLabel("Data Channels:");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new java.awt.Insets(0, 10, 10, 10);
        container.add(numericHeaderLabel, c);
        numericChannelList = new JList(channelModel);
        numericChannelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        numericChannelList.setCellRenderer(new CheckListRenderer());
        numericChannelList.setVisibleRowCount(10);
        numericChannelList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int index = numericChannelList.locationToIndex(e.getPoint());
                ExportChannel item = (ExportChannel) numericChannelList.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = numericChannelList.getCellBounds(index, index);
                numericChannelList.repaint(rect);
                checkSelectedChannels();
                updateTimeBounds();
            }
        });
        JScrollPane scrollPane = new JScrollPane(numericChannelList);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new java.awt.Insets(0, 10, 10, 10);
        container.add(scrollPane, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new java.awt.Insets(0, 10, 10, 5);
        container.add(new JLabel("Data file: "), c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        dataFileTextField = new JTextField(20);
        c.insets = new java.awt.Insets(0, 0, 10, 5);
        container.add(dataFileTextField, c);
        dataFileChooser = new JFileChooser();
        dataFileChooser.setDialogTitle("Select export file");
        dataFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        dataFileTextField.setText(dataFileChooser.getCurrentDirectory().getAbsolutePath() + File.separator + "data.dat");
        dataFileButton = new JButton("Browse");
        dataFileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                dataFileChooser.setSelectedFile(new File(dataFileTextField.getText()));
                int status = dataFileChooser.showDialog(ExportDialog.this, "OK");
                if (status == JFileChooser.APPROVE_OPTION) {
                    dataFileTextField.setText(dataFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 2;
        c.gridy = 5;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new java.awt.Insets(0, 0, 10, 10);
        container.add(dataFileButton, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new java.awt.Insets(0, 10, 10, 5);
        container.add(new JLabel("File format: "), c);
        fileFormatComboBox = new JComboBox(fileFormats.toArray());
        fileFormatComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                fileFormatUpdated();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new java.awt.Insets(0, 0, 10, 10);
        container.add(fileFormatComboBox, c);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Action exportAction = new AbstractAction() {

            private static final long serialVersionUID = -5356258138620428023L;

            public void actionPerformed(ActionEvent e) {
                ok();
            }
        };
        exportAction.putValue(Action.NAME, "Export");
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "export");
        actionMap.put("export", exportAction);
        exportButton = new JButton(exportAction);
        panel.add(exportButton);
        Action cancelAction = new AbstractAction() {

            private static final long serialVersionUID = -5868609501314154642L;

            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        };
        cancelAction.putValue(Action.NAME, "Cancel");
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        actionMap.put("cancel", cancelAction);
        cancelButton = new JButton(cancelAction);
        panel.add(cancelButton);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = GridBagConstraints.REMAINDER;
        ;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new java.awt.Insets(0, 0, 10, 5);
        container.add(panel, c);
        pack();
        if (getWidth() < 600) {
            setSize(600, getHeight());
        }
        dataFileTextField.requestFocusInWindow();
        setLocationByPlatform(true);
    }
