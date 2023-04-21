    public CustomConsolesPrefsPanel(ConsoleManager consoleManager) {
        if (consoleManager == null) throw new IllegalArgumentException("consoleManager may not be null");
        I18n i18n = I18n.get(CustomConsolesPrefsPanel.class);
        this.consoleManager = consoleManager;
        this.consoles = new DefaultListModel();
        this.consolesList = new JList(consoles);
        this.addConsoleButton = i18n.createButton("addConsoleButton");
        this.removeConsoleButton = i18n.createButton("removeConsoleButton");
        this.moveUpButton = i18n.createButton("moveUpButton");
        this.moveDownButton = i18n.createButton("moveDownButton");
        this.titleField = new JTextField();
        this.windowBox = new JComboBox(windowModel);
        this.encodingBox = new JComboBox(encodingModel);
        this.channelsField = new JTextField();
        this.addRemoveChannels = new AddRemoveButtons();
        this.messageRegexField = new JTextField();
        this.consolesListLabel = i18n.createLabel("consolesListLabel");
        this.consolesListScrollPane = new JScrollPane(consolesList);
        this.titleLabel = i18n.createLabel("titleLabel");
        this.windowLabel = i18n.createLabel("windowLabel");
        this.encodingLabel = i18n.createLabel("encodingLabel");
        this.channelsLabel = i18n.createLabel("channelsLabel");
        this.messageRegexLabel = i18n.createLabel("messageRegexLabel");
        Preferences prefs = consoleManager.getPrefs();
        for (int i = prefs.getInt("consoles.count") - 1; i > 0; i--) {
            String prefix = "consoles." + i + ".";
            if (!"custom".equals(prefs.getString(prefix + "type"))) break;
            consoles.add(0, loadConsoleSpec(prefs, prefix));
        }
        DocumentListener changeFiringDocumentListener = new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                if (!isIgnoreConsolePropertiesChange()) fireStateChanged();
            }

            public void insertUpdate(DocumentEvent e) {
                if (!isIgnoreConsolePropertiesChange()) fireStateChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                if (!isIgnoreConsolePropertiesChange()) fireStateChanged();
            }
        };
        consoles.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e) {
                fireStateChanged();
            }

            public void intervalAdded(ListDataEvent e) {
                fireStateChanged();
            }

            public void intervalRemoved(ListDataEvent e) {
                fireStateChanged();
            }
        });
        consolesList.addListSelectionListener(new ListSelectionListener() {

            private int selectedIndex = -1;

            public void valueChanged(ListSelectionEvent evt) {
                if (evt.getValueIsAdjusting()) return;
                if (isIgnoreConsoleListSelectionChange()) {
                    selectedIndex = consolesList.getSelectedIndex();
                    return;
                }
                if ((selectedIndex != -1) && (selectedIndex < consoles.size())) {
                    try {
                        updateConsoleFromUi((ConsoleSpec) consoles.get(selectedIndex));
                    } catch (BadChangesException e) {
                        try {
                            setIgnoreConsoleListSelectionChange(true);
                            consolesList.setSelectedIndex(selectedIndex);
                        } finally {
                            setIgnoreConsoleListSelectionChange(false);
                        }
                        badChangeAttempted(e);
                        return;
                    }
                }
                selectedIndex = consolesList.getSelectedIndex();
                updateUiFromSelectedConsole();
            }
        });
        consolesList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = consolesList.getSelectedIndex();
                removeConsoleButton.setEnabled(selectedIndex != -1);
                moveUpButton.setEnabled((selectedIndex != -1) && (selectedIndex > 0));
                moveDownButton.setEnabled((selectedIndex != -1) && (selectedIndex < consoles.size() - 1));
            }
        });
        consolesList.setVisibleRowCount(4);
        consolesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addConsoleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                consoles.addElement(createNewConsoleSpec());
                consolesList.setSelectedIndex(consoles.size() - 1);
                consolesList.ensureIndexIsVisible(consolesList.getSelectedIndex());
                updateUiFromSelectedConsole();
                titleField.requestFocusInWindow();
            }
        });
        removeConsoleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selectedIndex = consolesList.getSelectedIndex();
                if (selectedIndex == -1) return;
                try {
                    setIgnoreConsoleListSelectionChange(true);
                    consoles.removeElementAt(selectedIndex);
                    if (selectedIndex < consoles.size()) consolesList.setSelectedIndex(selectedIndex); else if (consoles.size() != 0) consolesList.setSelectedIndex(selectedIndex - 1);
                    consolesList.ensureIndexIsVisible(consolesList.getSelectedIndex());
                } finally {
                    setIgnoreConsoleListSelectionChange(false);
                }
                updateUiFromSelectedConsole();
                fireStateChanged();
            }
        });
        moveUpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selectedIndex = consolesList.getSelectedIndex();
                if ((selectedIndex == -1) || (selectedIndex == 0)) return;
                try {
                    setIgnoreConsoleListSelectionChange(true);
                    consoles.add(selectedIndex - 1, consoles.remove(selectedIndex));
                    consolesList.setSelectedIndex(selectedIndex - 1);
                    consolesList.ensureIndexIsVisible(consolesList.getSelectedIndex());
                } finally {
                    setIgnoreConsoleListSelectionChange(false);
                }
            }
        });
        moveDownButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selectedIndex = consolesList.getSelectedIndex();
                if ((selectedIndex == -1) || (selectedIndex == consoles.size() - 1)) return;
                try {
                    setIgnoreConsoleListSelectionChange(true);
                    consoles.add(selectedIndex + 1, consoles.remove(selectedIndex));
                    consolesList.setSelectedIndex(selectedIndex + 1);
                    consolesList.ensureIndexIsVisible(consolesList.getSelectedIndex());
                } finally {
                    setIgnoreConsoleListSelectionChange(false);
                }
            }
        });
        titleField.getDocument().addDocumentListener(changeFiringDocumentListener);
        titleField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                updateTitle();
            }

            public void insertUpdate(DocumentEvent e) {
                updateTitle();
            }

            public void removeUpdate(DocumentEvent e) {
                updateTitle();
            }

            private void updateTitle() {
                if (isIgnoreConsolePropertiesChange()) return;
                ConsoleSpec spec = getSelectedConsole();
                if (spec == null) return;
                spec.setTitle(titleField.getText());
                consolesList.repaint();
            }
        });
        windowModel.addElement(MAIN_WINDOW_NAME);
        int customWindowsCount = prefs.getInt("containers.custom.count", 0);
        for (int i = 0; i < customWindowsCount; i++) {
            String title = prefs.getString("containers.custom." + i + ".title", "");
            windowModel.addElement(title);
        }
        windowBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (isIgnoreConsolePropertiesChange()) return;
                ConsoleSpec spec = getSelectedConsole();
                if (spec == null) return;
                String windowTitle = (String) windowBox.getSelectedItem();
                spec.setWindow(windowTitle);
                if (windowModel.getIndexOf(windowTitle) == -1) windowModel.addElement(windowTitle);
                fireStateChanged();
            }
        });
        Component windowEditorComponent = windowBox.getEditor().getEditorComponent();
        if (windowEditorComponent instanceof JTextComponent) {
            JTextComponent textComponent = (JTextComponent) windowEditorComponent;
            textComponent.getDocument().addDocumentListener(changeFiringDocumentListener);
        }
        windowBox.setEditable(true);
        encodingModel.addElement(null);
        Map categoriesToEncodings = Encodings.categoriesToEncodings();
        Map categoriesToNames = Encodings.categoriesToNames();
        for (Iterator i = Encodings.categories().iterator(); i.hasNext(); ) {
            String category = (String) i.next();
            List encodings = (List) categoriesToEncodings.get(category);
            String categoryName = (String) categoriesToNames.get(category);
            if (encodings.isEmpty()) continue;
            encodingModel.addElement(categoryName);
            for (Iterator j = encodings.iterator(); j.hasNext(); ) {
                Charset encoding = (Charset) j.next();
                encodingModel.addElement(encoding);
            }
        }
        encodingBox.addActionListener(new ActionListener() {

            private Charset selectedEncoding;

            public void actionPerformed(ActionEvent evt) {
                if (isIgnoreConsolePropertiesChange()) return;
                Object item = encodingBox.getSelectedItem();
                if ((item instanceof Charset) || (item == null)) {
                    selectedEncoding = (Charset) item;
                    ConsoleSpec spec = getSelectedConsole();
                    if (spec == null) return;
                    spec.setEncoding(selectedEncoding == null ? null : selectedEncoding.name());
                    fireStateChanged();
                } else encodingBox.setSelectedItem(selectedEncoding);
            }
        });
        encodingBox.setRenderer(new EncodingBoxCellRenderer(encodingBox.getRenderer()));
        encodingBox.setEditable(false);
        if (consoleManager.getConn().getTextEncoding() == null) {
            encodingBox.setVisible(false);
            encodingLabel.setVisible(false);
        }
        channelsField.getDocument().addDocumentListener(changeFiringDocumentListener);
        addRemoveChannels.getAddButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final ConsoleSpec spec = (ConsoleSpec) consolesList.getSelectedValue();
                if (spec == null) return;
                ConsoleManager consoleManager = CustomConsolesPrefsPanel.this.consoleManager;
                SortedMap channels = new TreeMap(consoleManager.getChannels());
                channels.values().removeAll(spec.getChannels());
                Channel[] channelsArr = (Channel[]) channels.values().toArray(new Channel[channels.size()]);
                final ChannelsPopup popup = new ChannelsPopup(channelsArr);
                popup.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        popup.hide();
                        Channel channel = popup.getSelectedChannel();
                        spec.addChannel(channel);
                        updateUiFromSelectedConsole();
                        fireStateChanged();
                    }
                });
                popup.show(addRemoveChannels, 0, addRemoveChannels.getHeight());
            }
        });
        addRemoveChannels.getRemoveButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final ConsoleSpec spec = (ConsoleSpec) consolesList.getSelectedValue();
                if (spec == null) return;
                Channel[] channelsArr = (Channel[]) spec.getChannels().toArray(new Channel[0]);
                final ChannelsPopup popup = new ChannelsPopup(channelsArr);
                popup.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        popup.hide();
                        Channel channel = popup.getSelectedChannel();
                        spec.removeChannel(channel);
                        updateUiFromSelectedConsole();
                        fireStateChanged();
                    }
                });
                popup.show(addRemoveChannels, 0, addRemoveChannels.getHeight());
            }
        });
        messageRegexField.getDocument().addDocumentListener(changeFiringDocumentListener);
        consolesListLabel.setLabelFor(consolesList);
        titleLabel.setLabelFor(titleField);
        windowLabel.setLabelFor(windowBox);
        encodingLabel.setLabelFor(encodingBox);
        channelsLabel.setLabelFor(channelsField);
        messageRegexLabel.setLabelFor(messageRegexField);
        consolesListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        consolesListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
