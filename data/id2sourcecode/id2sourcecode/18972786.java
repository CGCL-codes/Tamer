    @Override
    protected void initComponents() {
        final IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();
        if (extractingManager.isCanceled() || cleaning) {
            return;
        }
        if (splitedData == null || splitedData[0] == null && splitedData[1] == null) {
            writable = ViewSpectrumTableModel.UNKNOWN;
            model = new ViewSpectrumTableModel(null, null);
        } else {
            viewButton = new JButton();
            viewButton.setMargin(new Insets(0, 0, 0, 0));
            viewButton.setIcon(VIEW_ICON);
            GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
            viewButton.addActionListener(new VCViewSpectrumChartAction(this));
            viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON") + " " + getName());
            viewButton.setEnabled(true);
            compareMode = new JCheckBox(Messages.getMessage("VIEW_SPECTRUM_COMPARE_MODE"));
            GUIUtilities.setObjectBackground(compareMode, GUIUtilities.VIEW_COLOR);
            compareMode.setSelected(false);
            compareMode.addItemListener(this);
            subtractMean = new JCheckBox(Messages.getMessage("VIEW_SPECTRUM_SUBTRACT_MEAN"));
            GUIUtilities.setObjectBackground(subtractMean, GUIUtilities.VIEW_COLOR);
            subtractMean.setSelected(false);
            subtractMean.addItemListener(this);
            final int viewType = getViewSpectrumType();
            String[] readNames = null;
            String[] writeNames = null;
            if (splitedData[0] != null) {
                allRead = new JButton(new VCViewSpectrumFilterAction(Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL") + " " + Messages.getMessage("VIEW_SPECTRUM_READ"), this, true, true));
                allRead.setMargin(new Insets(0, 0, 0, 0));
                allRead.setEnabled(true);
                noneRead = new JButton(new VCViewSpectrumFilterAction(Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE") + " " + Messages.getMessage("VIEW_SPECTRUM_READ"), this, false, true));
                noneRead.setMargin(new Insets(0, 0, 0, 0));
                noneRead.setEnabled(true);
                readNames = generateNames(splitedData[0], viewType, Messages.getMessage("VIEW_SPECTRUM_READ"));
                writable = ViewSpectrumTableModel.R;
            }
            if (splitedData[1] != null) {
                allWrite = new JButton(new VCViewSpectrumFilterAction(Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL") + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"), this, true, false));
                allWrite.setMargin(new Insets(0, 0, 0, 0));
                allWrite.setEnabled(true);
                noneWrite = new JButton(new VCViewSpectrumFilterAction(Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE") + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"), this, false, false));
                noneWrite.setMargin(new Insets(0, 0, 0, 0));
                noneWrite.setEnabled(true);
                writeNames = generateNames(splitedData[1], viewType, Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                writable = ViewSpectrumTableModel.W;
            }
            if (splitedData[0] != null && splitedData[1] != null) {
                writable = ViewSpectrumTableModel.RW;
            }
            model = new ViewSpectrumTableModel(readNames, writeNames);
            model.setViewSpectrumType(viewType);
            model.addTableModelListener(this);
            compareMode.setEnabled(model.getSelectedReadRowCount() == 2);
            buttonPanel = new JPanel();
            GUIUtilities.setObjectBackground(buttonPanel, GUIUtilities.VIEW_COLOR);
            buttonPanel.setLayout(new SpringLayout());
            switch(writable) {
                case ViewSpectrumTableModel.R:
                    buttonPanel.add(allRead);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(subtractMean);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(noneRead);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(compareMode);
                    buttonPanel.add(Box.createHorizontalGlue());
                    SpringUtilities.makeCompactGrid(buttonPanel, 2, 4, 5, 5, 5, 5, true);
                    break;
                case ViewSpectrumTableModel.W:
                    buttonPanel.add(allWrite);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(subtractMean);
                    buttonPanel.add(noneWrite);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(compareMode);
                    SpringUtilities.makeCompactGrid(buttonPanel, 2, 4, 5, 5, 5, 5, true);
                    break;
                case ViewSpectrumTableModel.RW:
                    buttonPanel.add(allRead);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(subtractMean);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(allWrite);
                    buttonPanel.add(noneRead);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(compareMode);
                    buttonPanel.add(Box.createHorizontalGlue());
                    buttonPanel.add(noneWrite);
                    SpringUtilities.makeCompactGrid(buttonPanel, 2, 5, 5, 5, 5, 5, true);
                    break;
                default:
                    break;
            }
            choiceTable = new JTable(model);
            choiceTable.setDefaultRenderer(Boolean.class, new ViewSpectrumTableBooleanRenderer());
            choiceTable.setFont(choiceTable.getFont().deriveFont(Font.BOLD));
            choiceTable.setMinimumSize(new Dimension(110, 200));
            GUIUtilities.setObjectBackground(choiceTable, GUIUtilities.VIEW_COLOR);
            choiceTableScrollPane = new JScrollPane(choiceTable);
            choiceTableScrollPane.setPreferredSize(REF_SIZE);
            choiceTableScrollPane.setMinimumSize(REF_SIZE);
            GUIUtilities.setObjectBackground(choiceTableScrollPane, GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(choiceTableScrollPane.getViewport(), GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(choiceTableScrollPane.getHorizontalScrollBar(), GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(choiceTableScrollPane.getVerticalScrollBar(), GUIUtilities.VIEW_COLOR);
        }
    }
