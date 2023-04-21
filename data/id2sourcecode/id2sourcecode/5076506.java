    private void setupViews(final WindowReference windowReference) {
        final JTable channelTable = (JTable) windowReference.getView("ChannelTable");
        channelTable.setDragEnabled(true);
        channelTable.setTransferHandler(new ChannelTransferHandler());
        CHANNEL_TABLE_MODEL = new ChannelTableModel(MODEL);
        channelTable.setModel(CHANNEL_TABLE_MODEL);
        channelTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        channelTable.getColumnModel().getColumn(ChannelTableModel.ENABLE_COLUMN).setMaxWidth(new JLabel(" Monitor ").getPreferredSize().width);
        channelTable.getColumnModel().getColumn(ChannelTableModel.PLOTTING_COLUMN).setMaxWidth(new JLabel(" Plot ").getPreferredSize().width);
        final JToggleButton playButton = (JToggleButton) windowReference.getView("PlayButton");
        playButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                MODEL.startCorrelator();
                updateRunStatus();
            }
        });
        final JToggleButton stopButton = (JToggleButton) windowReference.getView("StopButton");
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                MODEL.stopCorrelator();
                updateRunStatus();
            }
        });
        final ButtonGroup runButtonGroup = new ButtonGroup();
        runButtonGroup.add(playButton);
        runButtonGroup.add(stopButton);
        final JButton clearButton = (JButton) windowReference.getView("ClearButton");
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                clearPlot();
                synchronized (CORRELATION_BUFFER) {
                    CORRELATION_BUFFER.clear();
                }
            }
        });
        final JTextField correlationResolutionField = (JTextField) windowReference.getView("CorrelationResolutionField");
        correlationResolutionField.setText(String.valueOf(MODEL.getCorrelationResolution()));
        correlationResolutionField.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final String resolutionText = correlationResolutionField.getText();
                try {
                    final double resolution = Double.parseDouble(resolutionText);
                    MODEL.setCorrelationResolution(resolution);
                    DOCUMENT.setHasChanges(true);
                } catch (Exception exception) {
                    correlationResolutionField.setText(String.valueOf(MODEL.getCorrelationResolution()));
                }
            }
        });
        final SpinnerNumberModel bufferModel = (SpinnerNumberModel) BUFFER_SPINNER.getModel();
        bufferModel.setMinimum(2);
        bufferModel.setValue(_correlationPlotter.getBufferLimit());
        BUFFER_SPINNER.addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent event) {
                final int bufferSize = bufferModel.getNumber().intValue();
                final int bufferLimit = bufferSize >= 2 ? bufferSize : 2;
                setBufferLimit(bufferLimit);
                DOCUMENT.setHasChanges(true);
            }
        });
        final JButton exportButton = (JButton) windowReference.getView("ExportButton");
        exportButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                exportBuffer();
            }
        });
        final JButton insertChannelButton = (JButton) windowReference.getView("InsertChannelButton");
        insertChannelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final int selection = channelTable.getSelectedRow();
                if (selection < 0) {
                    MODEL.addChannelPlaceholder();
                } else {
                    MODEL.insertChannelPlaceholder(selection);
                }
                CHANNEL_TABLE_MODEL.fireTableDataChanged();
            }
        });
        final JButton addChannelRefButton = (JButton) windowReference.getView("AddChannelRefButton");
        addChannelRefButton.addActionListener(getChannelAddHandler(channelTable));
        final JButton deleteChannelButton = (JButton) windowReference.getView("DeleteChannelButton");
        deleteChannelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final int selection = channelTable.getSelectedRow();
                if (selection >= 0) {
                    MODEL.deleteChannelPlaceholder(selection);
                    CHANNEL_TABLE_MODEL.fireTableDataChanged();
                }
            }
        });
        final JButton clearFitButton = (JButton) windowReference.getView("ClearFitButton");
        clearFitButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                _correlationPlotter.clearFit();
            }
        });
        final JButton fitButton = (JButton) windowReference.getView("FitButton");
        fitButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                _correlationPlotter.performFit();
            }
        });
        final JButton copyFitButton = (JButton) windowReference.getView("CopyFitButton");
        copyFitButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final String equation = _correlationPlotter.getFitEquation();
                System.out.println("Copying Equation: " + equation);
                final StringSelection equationSelection = new StringSelection(equation);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(equationSelection, equationSelection);
            }
        });
        final SpinnerNumberModel fitOrderModel = (SpinnerNumberModel) FIT_ORDER_SPINNER.getModel();
        fitOrderModel.setMinimum(1);
        fitOrderModel.setMaximum(8);
        fitOrderModel.setValue(_correlationPlotter.getFitOrder());
        FIT_ORDER_SPINNER.addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent event) {
                final int fitOrder = fitOrderModel.getNumber().intValue();
                _correlationPlotter.setFitOrder(fitOrder >= 1 ? fitOrder : 1);
                DOCUMENT.setHasChanges(true);
            }
        });
    }
