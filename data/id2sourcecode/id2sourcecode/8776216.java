    public PhotoCameraPanel(PhotoWindow _window) {
        window = _window;
        cdWindow = (CDWindow) window.changer.getWindow(ChangerPanel.WINDOW_CDS);
        HPanel gphoto2InnerPanel = new HPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        HLabelComponent gphoto2Panel = new HLabelComponent("GPhoto2", gphoto2InnerPanel, true);
        gphoto2Panel.setOpaque(false);
        gphoto2InnerPanel.add(new HLabel("Command gphoto2"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, GUI.COMPONENT_HGAP, 0, 0);
        gphoto2Field = new JTextField(Config.getProperty(ConfigFile_v3.NODE_CAMERA, ConfigFile_v3.NODE_CAMERA_GPHOTO2));
        gphoto2InnerPanel.add(gphoto2Field, gbc);
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gphoto2Button = new HButton(Config.getIcon(Config.ICON_TYPE_FILE_OPEN, Config.ICON_SIZE_16));
        gphoto2InnerPanel.add(gphoto2Button, gbc);
        gphoto2Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser dirChooser = new JFileChooser();
                dirChooser.setDialogTitle(I18N.translate("Choose directory"));
                dirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                dirChooser.setSelectedFile(new File(gphoto2Field.getText()));
                int retValChooser = dirChooser.showOpenDialog(window);
                if (retValChooser == JFileChooser.APPROVE_OPTION) {
                    File file = dirChooser.getSelectedFile();
                    gphoto2Field.setText(file.getAbsolutePath());
                }
            }
        });
        downloadCommandArea = new JTextArea(3, 10);
        downloadCommandArea.setWrapStyleWord(true);
        downloadCommandArea.setLineWrap(true);
        downloadGenerateCheck = new HCheckBox("generate command from GUI");
        downloadGenerateCheck.setOpaque(false);
        HPanel downloadCommandPanel = new HPanel(new BorderLayout());
        downloadCommandPanel.setOpaque(false);
        downloadCommandPanel.add(new JScrollPane(downloadCommandArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        downloadCommandPanel.add(downloadGenerateCheck, BorderLayout.SOUTH);
        downloadCommandPanel.setAlignmentX(0.0f);
        downloadAllRadio = new HRadioButton("download all photos");
        downloadNewRadio = new HRadioButton("download only new photos");
        downloadRangeRadio = new HRadioButton("download photos in range");
        ButtonGroup downloadGroup = new ButtonGroup();
        downloadGroup.add(downloadAllRadio);
        downloadGroup.add(downloadNewRadio);
        downloadGroup.add(downloadRangeRadio);
        downloadAllRadio.setAlignmentX(0.0f);
        downloadNewRadio.setAlignmentX(0.0f);
        downloadRangeField = new JTextField();
        downloadRangeRadio.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, GUI.COMPONENT_HGAP));
        HPanel downloadRangePanel = new HPanel(new BorderLayout());
        downloadRangePanel.setOpaque(false);
        downloadRangePanel.add(downloadRangeRadio, BorderLayout.WEST);
        downloadRangePanel.add(downloadRangeField, BorderLayout.CENTER);
        downloadRangePanel.setAlignmentX(0.0f);
        downloadForceCheck = new HCheckBox("force overwrite");
        downloadForceCheck.setAlignmentX(0.0f);
        HPanel downloadComponentsPanel = new HPanel();
        downloadComponentsPanel.setOpaque(false);
        downloadComponentsPanel.setLayout(new BoxLayout(downloadComponentsPanel, BoxLayout.Y_AXIS));
        downloadComponentsPanel.add(downloadCommandPanel);
        downloadComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        downloadComponentsPanel.add(downloadAllRadio);
        downloadComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        downloadComponentsPanel.add(downloadNewRadio);
        downloadComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        downloadComponentsPanel.add(downloadRangePanel);
        downloadComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        downloadComponentsPanel.add(downloadForceCheck);
        downloadComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        HPanel downloadInnerPanel = new HPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        downloadInnerPanel.setOpaque(false);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        downloadButton = new HButton("Download", Config.getIcon(Config.ICON_DOWNLOAD_16));
        downloadInnerPanel.add(downloadComponentsPanel, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        downloadInnerPanel.add(downloadButton, gbc);
        HLabelComponent downloadPanel = new HLabelComponent("Download photos", downloadInnerPanel, true);
        downloadPanel.setOpaque(false);
        deleteCommandArea = new JTextArea(3, 10);
        deleteCommandArea.setWrapStyleWord(true);
        deleteCommandArea.setLineWrap(true);
        deleteGenerateCheck = new HCheckBox("generate command from GUI");
        deleteGenerateCheck.setOpaque(false);
        HPanel deleteCommandPanel = new HPanel(new BorderLayout());
        deleteCommandPanel.setOpaque(false);
        deleteCommandPanel.setAlignmentX(0.0f);
        deleteCommandPanel.add(new JScrollPane(deleteCommandArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        deleteCommandPanel.add(deleteGenerateCheck, BorderLayout.SOUTH);
        deleteAllRadio = new HRadioButton("delete all photos");
        deleteRangeRadio = new HRadioButton("delete photos in range");
        ButtonGroup deleteGroup = new ButtonGroup();
        deleteGroup.add(deleteAllRadio);
        deleteGroup.add(deleteRangeRadio);
        deleteAllRadio.setSelected(true);
        deleteAllRadio.setAlignmentX(0.0f);
        deleteRangeField = new JTextField();
        deleteRangeRadio.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, GUI.COMPONENT_HGAP));
        HPanel deleteRangePanel = new HPanel(new BorderLayout());
        deleteRangePanel.setOpaque(false);
        deleteRangePanel.add(deleteRangeRadio, BorderLayout.WEST);
        deleteRangePanel.add(deleteRangeField, BorderLayout.CENTER);
        deleteRangePanel.setAlignmentX(0.0f);
        HPanel deleteComponentsPanel = new HPanel();
        deleteComponentsPanel.setOpaque(false);
        deleteComponentsPanel.setLayout(new BoxLayout(deleteComponentsPanel, BoxLayout.Y_AXIS));
        deleteComponentsPanel.add(deleteCommandPanel);
        deleteComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        deleteComponentsPanel.add(deleteAllRadio);
        deleteComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        deleteComponentsPanel.add(deleteRangePanel);
        deleteComponentsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        HPanel deleteInnerPanel = new HPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        deleteInnerPanel.setOpaque(false);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        deleteButton = new HButton("Delete", Config.getIcon(Config.ICON_TYPE_TRASH, Config.ICON_SIZE_16));
        deleteInnerPanel.add(deleteComponentsPanel, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        deleteInnerPanel.add(deleteButton, gbc);
        HLabelComponent deletePanel = new HLabelComponent("Delete photos", deleteInnerPanel, true);
        deletePanel.setOpaque(false);
        HPanel leftTopPanel = new HPanel();
        leftTopPanel.setLayout(new BoxLayout(leftTopPanel, BoxLayout.Y_AXIS));
        leftTopPanel.setOpaque(false);
        leftTopPanel.add(gphoto2Panel);
        leftTopPanel.add(Box.createVerticalStrut(GUI.PANEL_VGAP));
        leftTopPanel.add(downloadPanel);
        leftTopPanel.add(Box.createVerticalStrut(GUI.PANEL_VGAP));
        leftTopPanel.add(deletePanel);
        HPanel leftPanel = new HPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, GUI.PANEL_HGAP));
        leftPanel.setOpaque(false);
        leftPanel.add(leftTopPanel, BorderLayout.NORTH);
        JPanel leftHolePanel = new JPanel();
        leftHolePanel.setOpaque(false);
        leftPanel.add(leftHolePanel, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(290, leftPanel.getPreferredSize().height));
        HPanel albumNamePanel = new HPanel();
        gbc = new GridBagConstraints();
        albumNamePanel.setLayout(new GridBagLayout());
        albumNamePanel.setOpaque(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        albumNamePanel.add(new HLabel("Album name"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, GUI.COMPONENT_HGAP, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        albumCombo = new JComboBox(getAllAlbums());
        albumCombo.setEditor(new HComboBoxEditor());
        albumCombo.setEditable(true);
        albumNamePanel.add(albumCombo, gbc);
        addAlbumButton = new HButton(Config.getIcon(Config.ICON_TYPE_ADD, Config.ICON_SIZE_16));
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        albumNamePanel.add(addAlbumButton, gbc);
        HPanel filesInnerPanel = new HPanel();
        filesInnerPanel.setLayout(new BorderLayout());
        filesInnerPanel.setOpaque(false);
        filesListModel = new DefaultListModel();
        filesList = new JList(filesListModel);
        JScrollPane filesScroll = new JScrollPane(filesList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filesList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof File) {
                    File file = (File) value;
                    setText(file.getName());
                }
                return this;
            }
        });
        HPanel filesButtonsPanel = new HPanel();
        filesButtonsPanel.setLayout(new BoxLayout(filesButtonsPanel, BoxLayout.Y_AXIS));
        filesButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0, GUI.COMPONENT_HGAP, 0, 0));
        filesButtonsPanel.setOpaque(false);
        addButton = new HButton(Config.getIcon(Config.ICON_TYPE_ADD, Config.ICON_SIZE_16));
        delButton = new HButton(Config.getIcon(Config.ICON_TYPE_REMOVE, Config.ICON_SIZE_16));
        upButton = new HButtonScroll(Config.getIcon(Config.ICON_TYPE_PREV_VERTICAL, Config.ICON_SIZE_16));
        downButton = new HButtonScroll(Config.getIcon(Config.ICON_TYPE_NEXT_VERTICAL, Config.ICON_SIZE_16));
        refreshButton = new HButton(Config.getIcon(Config.ICON_TYPE_REFRESH, Config.ICON_SIZE_16));
        filesButtonsPanel.add(addButton);
        filesButtonsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        filesButtonsPanel.add(delButton);
        filesButtonsPanel.add(Box.createVerticalStrut(GUI.PANEL_VGAP));
        filesButtonsPanel.add(upButton);
        filesButtonsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        filesButtonsPanel.add(downButton);
        filesButtonsPanel.add(Box.createVerticalStrut(GUI.PANEL_VGAP));
        filesButtonsPanel.add(refreshButton);
        filesButtonsPanel.add(Box.createVerticalGlue());
        filesInnerPanel.add(filesScroll, BorderLayout.CENTER);
        filesInnerPanel.add(filesButtonsPanel, BorderLayout.EAST);
        HLabelComponent filesPanel = new HLabelComponent("New photos", filesInnerPanel);
        filesPanel.setBorder(BorderFactory.createEmptyBorder(GUI.PANEL_VGAP, 0, GUI.COMPONENT_VGAP, 0));
        filesPanel.setOpaque(false);
        HPanel buttonsPanel = new HPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        camRotateButton = new HButton(I18N.translate("rotate - adjust"));
        camSortButton = new HButton(I18N.translate("sort by date"));
        camRenameButton = new HButton(I18N.translate("rename - enumerate"));
        camRotateButton.setAlignmentX(0.5f);
        camSortButton.setAlignmentX(0.5f);
        camRenameButton.setAlignmentX(0.5f);
        Dimension maxDim = cz.hdf.util.Utils.getMaximumWidth(new Dimension[] { camRotateButton.getMaximumSize(), camSortButton.getMaximumSize(), camRenameButton.getMaximumSize() });
        camRotateButton.setMaximumSize(maxDim);
        camSortButton.setMaximumSize(maxDim);
        camRenameButton.setMaximumSize(maxDim);
        buttonsPanel.add(camRotateButton);
        buttonsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        buttonsPanel.add(camSortButton);
        buttonsPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        buttonsPanel.add(camRenameButton);
        HPanel albumInnerPanel = new HPanel();
        albumInnerPanel.setLayout(new BorderLayout());
        albumInnerPanel.add(albumNamePanel, BorderLayout.NORTH);
        albumInnerPanel.add(filesPanel, BorderLayout.CENTER);
        albumInnerPanel.add(buttonsPanel, BorderLayout.SOUTH);
        HLabelComponent albumPanel = new HLabelComponent("New album in Temp/Roast folder", albumInnerPanel, true);
        albumPanel.setOpaque(false);
        albumPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, GUI.PANEL_VGAP, 0));
        HPanel photoInnerPanel = new HPanel();
        photoInnerPanel.setLayout(new BorderLayout());
        photoInnerPanel.setOpaque(false);
        HPanel thumbPanel = new HPanel();
        thumbPanel.setLayout(new BorderLayout());
        thumbPanel.setOpaque(false);
        thumbPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        thumbImagePanel = new HImagePanel(null, Global.THUMB_IMAGE_SIZE);
        thumbImagePanel.setPreferredSize(Global.THUMB_IMAGE_SIZE);
        thumbPanel.add(thumbImagePanel, BorderLayout.CENTER);
        HPanel thumbGapPanel = new HPanel(new BorderLayout());
        thumbGapPanel.add(thumbPanel, BorderLayout.NORTH);
        HPanel rotatePanel = new HPanel();
        rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.X_AXIS));
        rotatePanel.setOpaque(false);
        rotatePanel.setAlignmentX(0.0f);
        rotateLeftButton = new HButtonNoted("Left", Config.getIcon(Config.ICON_TYPE_ROTATE_LEFT, Config.ICON_SIZE_16));
        rotateRightButton = new HButtonNoted("Right", Config.getIcon(Config.ICON_TYPE_ROTATE_RIGHT, Config.ICON_SIZE_16));
        maxDim = cz.hdf.util.Utils.getMaximumWidth(new Dimension[] { rotateLeftButton.getMaximumSize(), rotateRightButton.getMaximumSize() });
        rotateLeftButton.setMaximumSize(maxDim);
        rotateRightButton.setMaximumSize(maxDim);
        rotateLabel = new HLabel("Rotate photo");
        rotatePanel.add(rotateLabel);
        rotatePanel.add(Box.createHorizontalStrut(GUI.COMPONENT_HGAP));
        rotatePanel.add(rotateLeftButton);
        rotatePanel.add(Box.createHorizontalStrut(GUI.COMPONENT_HGAP));
        rotatePanel.add(rotateRightButton);
        HPanel orientPanel = new HPanel();
        orientPanel.setLayout(new BorderLayout(GUI.COMPONENT_HGAP, 0));
        orientPanel.setOpaque(false);
        orientPanel.setAlignmentX(0.0f);
        orientLabel = new HLabel("Exif orientation");
        orientPanel.add(orientLabel, BorderLayout.WEST);
        orientCombo = new JComboBox(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, -1 });
        orientCombo.setRenderer(new DefaultListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value != null) {
                    return super.getListCellRendererComponent(list, JPEG.getEXIFOrientationDescription((Integer) value), index, isSelected, cellHasFocus);
                } else {
                    return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
                }
            }
        });
        orientPanel.add(orientCombo, BorderLayout.CENTER);
        orientButton = new HButtonNoted("Change", Config.getIcon(Config.ICON_APPLY_CHANGE_16));
        orientPanel.add(orientButton, BorderLayout.EAST);
        HPanel timePanel = new HPanel(new BorderLayout(GUI.COMPONENT_HGAP, 0));
        timePanel.setOpaque(false);
        timePanel.setAlignmentX(0.0f);
        timeLabel = new HLabel("Time");
        timeChooser = new JDateChooser();
        timeChooser.setDateFormatString("HH:mm:ss dd.MM.yyyy");
        timeChooser.setDate(new Date(System.currentTimeMillis()));
        timeChooser.setOpaque(false);
        timeButton = new HButtonNoted("Change", Config.getIcon(Config.ICON_APPLY_CHANGE_16));
        timePanel.add(timeLabel, BorderLayout.WEST);
        timePanel.add(timeChooser, BorderLayout.CENTER);
        timePanel.add(timeButton, BorderLayout.EAST);
        shiftLabel = new HLabel("Time shift (days, hours, mins, secs)");
        shiftDaySpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        shiftDaySpinner.setOpaque(false);
        shiftDaySpinner.setToolTipText(I18N.translate("Days"));
        shiftHourSpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        shiftHourSpinner.setOpaque(false);
        shiftHourSpinner.setToolTipText(I18N.translate("Hours"));
        shiftMinSpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        shiftMinSpinner.setOpaque(false);
        shiftMinSpinner.setToolTipText(I18N.translate("Minutes"));
        shiftSecSpinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        shiftSecSpinner.setOpaque(false);
        shiftSecSpinner.setToolTipText(I18N.translate("Seconds"));
        HPanel shiftSpinnerPanel = new HPanel();
        shiftSpinnerPanel.setLayout(new GridLayout(1, 0));
        shiftSpinnerPanel.add(shiftDaySpinner);
        shiftSpinnerPanel.add(shiftHourSpinner);
        shiftSpinnerPanel.add(shiftMinSpinner);
        shiftSpinnerPanel.add(shiftSecSpinner);
        shiftButton = new HButtonNoted("Shift", Config.getIcon(Config.ICON_TIME_SHIFT_16));
        HPanel shiftPanel = new HPanel(new GridBagLayout());
        shiftPanel.setAlignmentX(0.0f);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        shiftPanel.add(shiftLabel, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        shiftPanel.add(shiftSpinnerPanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets.left = GUI.COMPONENT_HGAP;
        shiftPanel.add(shiftButton, gbc);
        HPanel photoRightInnerPanel = new HPanel();
        photoRightInnerPanel.setLayout(new BoxLayout(photoRightInnerPanel, BoxLayout.Y_AXIS));
        photoRightInnerPanel.setOpaque(false);
        photoRightInnerPanel.add(rotatePanel);
        photoRightInnerPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        photoRightInnerPanel.add(orientPanel);
        photoRightInnerPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        photoRightInnerPanel.add(timePanel);
        photoRightInnerPanel.add(Box.createVerticalStrut(GUI.COMPONENT_VGAP));
        photoRightInnerPanel.add(shiftPanel);
        HPanel photoRightPanel = new HPanel();
        photoRightPanel.setBorder(BorderFactory.createEmptyBorder(0, GUI.PANEL_HGAP, 0, 0));
        photoRightPanel.setLayout(new BorderLayout());
        photoRightPanel.setOpaque(false);
        photoRightPanel.add(photoRightInnerPanel, BorderLayout.NORTH);
        photoInnerPanel.add(thumbGapPanel, BorderLayout.WEST);
        photoInnerPanel.add(photoRightPanel, BorderLayout.CENTER);
        HLabelComponent photoPanel = new HLabelComponent("Selected photo", photoInnerPanel, true);
        photoPanel.setOpaque(false);
        HPanel rightPanel = new HPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(albumPanel, BorderLayout.CENTER);
        rightPanel.add(photoPanel, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        setOpaque(false);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(GUI.DIALOG_VGAP, GUI.DIALOG_HGAP, GUI.DIALOG_VGAP, GUI.DIALOG_HGAP));
        String value = Config.getProperty(ConfigFile_v3.NODE_CAMERA, ConfigFile_v3.NODE_CAMERA_DOWNLOAD_COMMAND);
        if ((value != null) && (!value.equals(""))) {
            downloadCommandArea.setText(value);
            downloadGenerateCheck.setSelected(false);
        } else {
            downloadGenerateCheck.setSelected(true);
        }
        value = Config.getProperty(ConfigFile_v3.NODE_CAMERA, ConfigFile_v3.NODE_CAMERA_DOWNLOAD_PHOTOS);
        downloadAllRadio.setSelected(true);
        if (value != null) {
            if (value.equalsIgnoreCase(DOWNLOAD_PHOTOS_NEW)) {
                downloadNewRadio.setSelected(true);
            } else if (value.equalsIgnoreCase(DOWNLOAD_PHOTOS_RANGE)) {
                downloadRangeRadio.setSelected(true);
            }
        }
        value = Config.getProperty(ConfigFile_v3.NODE_CAMERA, ConfigFile_v3.NODE_CAMERA_DOWNLOAD_FORCE);
        if ((value != null) && (value.equalsIgnoreCase("false"))) {
            downloadForceCheck.setSelected(false);
        } else {
            downloadForceCheck.setSelected(true);
        }
        value = Config.getProperty(ConfigFile_v3.NODE_CAMERA, ConfigFile_v3.NODE_CAMERA_DELETE_COMMAND);
        if ((value != null) && (!value.equals(""))) {
            deleteCommandArea.setText(value);
            deleteGenerateCheck.setSelected(false);
        } else {
            deleteGenerateCheck.setSelected(true);
        }
        value = Config.getProperty(ConfigFile_v3.NODE_CAMERA, ConfigFile_v3.NODE_CAMERA_DELETE_PHOTOS);
        deleteAllRadio.setSelected(true);
        if ((value != null) && (value.equalsIgnoreCase(DELETE_PHOTOS_RANGE))) {
            deleteRangeRadio.setSelected(true);
        }
        generateDownloadCommand();
        updateDownloadState();
        generateDeleteCommand();
        updateDeleteState();
        loadAlbumFiles();
        updateThumb();
        downloadGenerateCheck.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateDownloadState();
                saveDownloadSettings();
            }
        });
        ChangeListener downloadChangeLsnr = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                generateDownloadCommand();
                saveDownloadSettings();
            }
        };
        DocumentListener downloadDocumentLsnr = new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                generateDownloadCommand();
                saveDownloadSettings();
            }

            public void insertUpdate(DocumentEvent e) {
                generateDownloadCommand();
                saveDownloadSettings();
            }

            public void changedUpdate(DocumentEvent e) {
                generateDownloadCommand();
                saveDownloadSettings();
            }
        };
        downloadAllRadio.addChangeListener(downloadChangeLsnr);
        downloadNewRadio.addChangeListener(downloadChangeLsnr);
        downloadRangeRadio.addChangeListener(downloadChangeLsnr);
        gphoto2Field.getDocument().addDocumentListener(downloadDocumentLsnr);
        downloadRangeField.getDocument().addDocumentListener(downloadDocumentLsnr);
        downloadForceCheck.addChangeListener(downloadChangeLsnr);
        downloadCommandArea.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                saveDownloadSettings();
            }

            public void insertUpdate(DocumentEvent e) {
                saveDownloadSettings();
            }

            public void changedUpdate(DocumentEvent e) {
                saveDownloadSettings();
            }
        });
        deleteGenerateCheck.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateDeleteState();
                saveDeleteSettings();
            }
        });
        ChangeListener deleteChangeLsnr = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                generateDeleteCommand();
                saveDeleteSettings();
            }
        };
        DocumentListener deleteDocumentLsnr = new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                generateDeleteCommand();
                saveDeleteSettings();
            }

            public void insertUpdate(DocumentEvent e) {
                generateDeleteCommand();
                saveDeleteSettings();
            }

            public void changedUpdate(DocumentEvent e) {
                generateDeleteCommand();
                saveDeleteSettings();
            }
        };
        deleteAllRadio.addChangeListener(deleteChangeLsnr);
        deleteRangeRadio.addChangeListener(deleteChangeLsnr);
        gphoto2Field.getDocument().addDocumentListener(deleteDocumentLsnr);
        deleteRangeField.getDocument().addDocumentListener(deleteDocumentLsnr);
        deleteCommandArea.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                saveDeleteSettings();
            }

            public void insertUpdate(DocumentEvent e) {
                saveDeleteSettings();
            }

            public void changedUpdate(DocumentEvent e) {
                saveDeleteSettings();
            }
        });
        downloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final File dir = getAlbumDirectory();
                if ((dir == null) || !dir.exists() || !dir.isDirectory()) {
                    new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't get album folder."));
                    return;
                }
                HProgressDialog progressDialog = new HProgressDialog(window.changer.topFrame, I18N.translate("Download photos"), true, true);
                HProgressRunnable progressRunnable = new HProgressRunnableDialog(progressDialog) {

                    private Process process;

                    private HProcessReader progressInput = null;

                    private HProcessReader progressError = null;

                    public void run() {
                        try {
                            dialog.printlnAction(I18N.translate("Executing"));
                            dialog.println(downloadCommandArea.getText());
                            logger.info("Executing: " + downloadCommandArea.getText());
                            process = Runtime.getRuntime().exec(downloadCommandArea.getText(), null, dir);
                            progressInput = new HProcessReader(process.getInputStream(), dialog);
                            progressError = new HProcessReader(process.getErrorStream(), dialog);
                            ThreadManager.addTask(progressInput, "Gphoto2 Read InputStream");
                            ThreadManager.addTask(progressError, "Gphoto2 Read ErrorStream");
                            int rc = process.waitFor();
                            if (rc != 0) {
                                loadAlbumFiles();
                                cdWindow.cdTempRoastPanel.refreshDirectory();
                                dialog.close();
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't download photos from camera.") + Config.LS + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                                return;
                            } else {
                                dialog.println(I18N.translate("Photos successfully downloaded."));
                            }
                        } catch (IllegalArgumentException exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't download photos from camera.") + Config.LS + Config.LS + exc.getMessage() + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                        } catch (IOException exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't download photos from camera.") + Config.LS + Config.LS + exc.getMessage() + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                        } catch (InterruptedException exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't download photos from camera.") + Config.LS + Config.LS + exc.getMessage() + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                        }
                        loadAlbumFiles();
                        cdWindow.cdTempRoastPanel.refreshDirectory();
                        dialog.close();
                    }

                    public void stop() {
                        progressInput.close();
                        progressError.close();
                        process.destroy();
                    }
                };
                progressDialog.setRunnable(progressRunnable);
                ThreadManager.addTask(progressRunnable, "Executing");
                progressDialog.showDialog();
            }
        });
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                HProgressDialog progressDialog = new HProgressDialog(window.changer.topFrame, I18N.translate("Delete photos"), true, true);
                HProgressRunnable progressRunnable = new HProgressRunnableDialog(progressDialog) {

                    private Process process;

                    private HProcessReader progressInput = null;

                    private HProcessReader progressError = null;

                    public void run() {
                        try {
                            dialog.printlnAction("Executing");
                            dialog.println(deleteCommandArea.getText());
                            logger.info("Executing: " + deleteCommandArea.getText());
                            process = Runtime.getRuntime().exec(deleteCommandArea.getText());
                            progressInput = new HProcessReader(process.getInputStream(), dialog);
                            progressError = new HProcessReader(process.getErrorStream(), dialog);
                            ThreadManager.addTask(progressInput, "Gphoto2 Read InputStream");
                            ThreadManager.addTask(progressError, "Gphoto2 Read ErrorStream");
                            int rc = process.waitFor();
                            if (rc != 0) {
                                dialog.close();
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't delete photos from camera.") + Config.LS + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                                return;
                            } else {
                                dialog.println(I18N.translate("Photos successfully deleted."));
                            }
                        } catch (IllegalArgumentException exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't delete photos from camera.") + Config.LS + Config.LS + exc.getMessage() + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                        } catch (IOException exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't delete photos from camera.") + Config.LS + Config.LS + exc.getMessage() + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                        } catch (InterruptedException exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't delete photos from camera.") + Config.LS + Config.LS + exc.getMessage() + Config.LS + progressInput.getReadedData() + Config.LS + progressError.getReadedData());
                        }
                        loadAlbumFiles();
                        cdWindow.cdTempRoastPanel.refreshDirectory();
                        dialog.close();
                    }

                    public void stop() {
                        progressInput.close();
                        progressError.close();
                        process.destroy();
                    }
                };
                progressDialog.setRunnable(progressRunnable);
                ThreadManager.addTask(progressRunnable, "Executing");
                progressDialog.showDialog();
            }
        });
        filesList.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getModifiers() == 0) && (e.getKeyCode() == KeyEvent.VK_DELETE)) {
                    deleteSelectedPhotos();
                }
            }
        });
        filesList.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu("Context menu");
                if (!SwingUtilities.isRightMouseButton(e)) {
                    menu.setVisible(false);
                    return;
                }
                final Object[] filesObj = filesListModel.toArray();
                if (filesObj.length <= 0) {
                    return;
                }
                JMenuItem menuItemShow = new JMenuItem(I18N.translate("Show photos"));
                menu.add(menuItemShow);
                menuItemShow.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ee) {
                        PhotoWindow photoWindow = (PhotoWindow) window.changer.getWindow(ChangerPanel.WINDOW_PHOTOS);
                        photoWindow.stFoundTree.clearSelection();
                        DefaultMutableTreeNode rootPhoto = new DefaultMutableTreeNode("root");
                        photoWindow.stFoundTreeModel.setRoot(rootPhoto);
                        photoWindow.photoInfoPanel.ptPrevAlbumButton.setEnabled(false);
                        photoWindow.photoInfoPanel.ptNextAlbumButton.setEnabled(false);
                        photoWindow.photoInfoPanel.ptPrevPhotoButton.setEnabled(false);
                        photoWindow.photoInfoPanel.ptNextPhotoButton.setEnabled(false);
                        photoWindow.displayActualPhoto();
                        photoWindow.displayActualAlbum();
                        DefaultMutableTreeNode albumPhotoNode = new DefaultMutableTreeNode(new FoundedPhotoListRecord(FoundedRecord.TYPE_ROAST, null, new PhotoAlbumData(-1, PhotoAlbumData.BAD_INT_VALUE, (String) albumCombo.getSelectedItem(), null, new Integer(Utils.RATING_0).intValue())));
                        rootPhoto.add(albumPhotoNode);
                        String roastDir = Config.getProperty(ConfigFile_v3.NODE_ROAST, ConfigFile_v3.NODE_ROAST_DIRECTORY);
                        File file;
                        for (int i = 0; i < filesObj.length; i++) {
                            file = (File) filesObj[i];
                            if (Utils.isPhotoFile(file)) {
                                albumPhotoNode.add(new DefaultMutableTreeNode(new FoundedPhotoListRecord(FoundedRecord.TYPE_ROAST, new PhotoData(PhotoData.BAD_INT_VALUE, -1, i + 1, Utils.removeDirPrefix(file.getAbsolutePath(), roastDir), null, new Integer(Utils.RATING_0).intValue(), null, null, -1, -1, null, null, null, null, null, null, null, null, null, null, null), null)));
                            }
                        }
                        photoWindow.stFoundTreeModel.nodeStructureChanged(rootPhoto);
                        photoWindow.stFoundTree.setRootVisible(true);
                        photoWindow.stFoundTree.expandRow(0);
                        photoWindow.stFoundTree.expandRow(1);
                        int selectionIndex = filesList.getSelectedIndex();
                        if (selectionIndex == -1) {
                            photoWindow.stFoundTree.setSelectionRow(2);
                        } else {
                            photoWindow.stFoundTree.setSelectionRow(2 + selectionIndex);
                        }
                        photoWindow.stFoundTree.setRootVisible(false);
                        photoWindow.showPhotoPanel.displayRealPhotos();
                    }
                });
                JMenuItem menuItemDelete = new JMenuItem(I18N.translate("Delete"));
                menu.add(menuItemDelete);
                menuItemDelete.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ee) {
                        deleteSelectedPhotos();
                    }
                });
                JMenu menuMove = new JMenu(I18N.translate("Move to"));
                menu.add(menuMove);
                final String tempRoastDir = Config.getProperty(ConfigFile_v3.NODE_ROAST, ConfigFile_v3.NODE_ROAST_DIRECTORY);
                String[] albums = getAllAlbums();
                Arrays.sort(albums);
                for (final String album : albums) {
                    if (album.equals(albumCombo.getSelectedItem())) {
                        continue;
                    }
                    JMenuItem menuItemMove = new JMenuItem(album);
                    menuItemMove.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent ee) {
                            File fileSel;
                            File folderDest = new File(tempRoastDir, album);
                            File fileDest;
                            String errMsg = "";
                            Object[] filesSelected = filesList.getSelectedValues();
                            for (int i = 0; i < filesSelected.length; i++) {
                                fileSel = (File) filesSelected[i];
                                try {
                                    fileDest = new File(folderDest, fileSel.getName());
                                    if (!fileSel.renameTo(fileDest)) {
                                        errMsg += ", " + fileSel.getName();
                                    }
                                } catch (Exception exc) {
                                    errMsg += ", " + fileSel.getName();
                                }
                            }
                            if (!errMsg.equals("")) {
                                errMsg = errMsg.substring(2);
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't move these photos.") + Config.LS + Config.LS + errMsg);
                            }
                            loadAlbumFiles();
                            cdWindow.cdTempRoastPanel.refreshDirectory();
                        }
                    });
                    menuMove.add(menuItemMove);
                }
                menu.show(filesList, e.getX(), e.getY());
            }
        });
        filesList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                updateThumb();
            }
        });
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final File dir = getAlbumDirectory();
                if ((dir == null) || !dir.exists() || !dir.isDirectory()) {
                    new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't get album folder."));
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle(I18N.translate("Choose files"));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileFilter(Utils.PHOTOS_FILE_FILTER_SWING);
                String defaultDir = Config.getProperty(Config.NODE_DEFAULT, Config.NODE_DEFAULT_DIR_PHOTOS);
                fileChooser.setCurrentDirectory((defaultDir == null) ? null : new File(defaultDir));
                int retValChooser = fileChooser.showOpenDialog(window);
                Config.setProperty(Config.NODE_DEFAULT, Config.NODE_DEFAULT_DIR_PHOTOS, fileChooser.getCurrentDirectory().getAbsolutePath());
                if (retValChooser == JFileChooser.APPROVE_OPTION) {
                    final File[] importFiles = fileChooser.getSelectedFiles();
                    long size = 0;
                    for (int i = 0; i < importFiles.length; i++) {
                        if (importFiles[i].isDirectory()) {
                            continue;
                        }
                        size += importFiles[i].length();
                    }
                    final long totalSize = size;
                    HProgressDialog progressDialog = new HProgressDialog(window.changer.topFrame, I18N.translate("Add files"), true, false);
                    HProgressRunnable copyTask = new HProgressRunnableDialog(progressDialog) {

                        public void run() {
                            int i = 0;
                            long size = 0;
                            File newFile;
                            List<File> filesErr = new ArrayList<File>();
                            dialog.printlnAction(I18N.translate("Copy files"));
                            for (; i < importFiles.length; i++) {
                                if (stop) {
                                    break;
                                }
                                if (importFiles[i].isDirectory()) {
                                    continue;
                                }
                                dialog.println(I18N.translate("Copying") + " " + importFiles[i]);
                                newFile = new File(dir, importFiles[i].getName());
                                if (!newFile.exists()) {
                                    try {
                                        cz.hdf.util.Utils.copyFile(importFiles[i], newFile);
                                        filesListModel.addElement(newFile);
                                    } catch (IOException e) {
                                        dialog.println(I18N.translate("Can not copy file '{0}'.", importFiles[i].getName()));
                                        filesErr.add(importFiles[i]);
                                    }
                                } else {
                                    dialog.printlnError(I18N.translate("File '{0}' already exist. Skipping them.", newFile.getName()));
                                }
                                size += importFiles[i].length();
                                dialog.setPercent(new Long((size * 100) / totalSize).intValue());
                            }
                            cdWindow.cdTempRoastPanel.refreshDirectory();
                            window.changer.topFrame.setCursor(Cursor.getDefaultCursor());
                            dialog.close();
                            if (!filesErr.isEmpty()) {
                                String filesStr = "";
                                for (File fileErr : filesErr) {
                                    filesStr += fileErr.getName() + " ";
                                }
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can not copy files '{0}'.", filesStr.trim()));
                            }
                        }
                    };
                    progressDialog.setRunnable(copyTask);
                    ThreadManager.addTask(copyTask, "Copying");
                    progressDialog.showDialog();
                }
            }
        });
        delButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteSelectedPhotos();
            }
        });
        upButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ListSelectionListener[] lsnrs = filesList.getListSelectionListeners();
                        for (ListSelectionListener lsnr : lsnrs) {
                            filesList.removeListSelectionListener(lsnr);
                        }
                        int[] indexes = filesList.getSelectedIndices();
                        if ((indexes == null) || (indexes.length == 0) || (indexes[0] <= 0)) {
                            return;
                        }
                        for (int i = 0; i < indexes.length; i++) {
                            Object item = filesListModel.remove(indexes[i]);
                            filesListModel.add(indexes[i] - 1, item);
                            indexes[i]--;
                        }
                        filesList.setSelectedIndices(indexes);
                        filesList.ensureIndexIsVisible(indexes[0]);
                        for (ListSelectionListener lsnr : lsnrs) {
                            filesList.addListSelectionListener(lsnr);
                        }
                    }
                });
            }
        });
        downButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ListSelectionListener[] lsnrs = filesList.getListSelectionListeners();
                        for (ListSelectionListener lsnr : lsnrs) {
                            filesList.removeListSelectionListener(lsnr);
                        }
                        int[] indexes = filesList.getSelectedIndices();
                        if ((indexes == null) || (indexes.length == 0) || (indexes[indexes.length - 1] >= (filesListModel.getSize() - 1))) {
                            return;
                        }
                        Integer[] newPositions = new Integer[indexes.length];
                        for (int i = indexes.length - 1; i >= 0; i--) {
                            Object item = filesListModel.remove(indexes[i]);
                            filesListModel.add(indexes[i] + 1, item);
                            indexes[i]++;
                            newPositions[i] = indexes[i];
                        }
                        filesList.setSelectedIndices(indexes);
                        filesList.ensureIndexIsVisible(indexes[indexes.length - 1]);
                        for (ListSelectionListener lsnr : lsnrs) {
                            filesList.addListSelectionListener(lsnr);
                        }
                    }
                });
            }
        });
        refreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadAlbumFiles();
                cdWindow.cdTempRoastPanel.refreshDirectory();
            }
        });
        camRotateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                File file;
                final ArrayList<File> files = new ArrayList<File>();
                for (Object fileObj : filesListModel.toArray()) {
                    file = (File) fileObj;
                    if (Utils.isPhotoFile(file)) {
                        if (needRotateAccordingOrientation(file)) {
                            files.add(file);
                        }
                    }
                }
                if (!files.isEmpty()) {
                    HProgressDialog progressDialog = new HProgressDialog(window.changer.topFrame, I18N.translate("Rotate files"), true, false);
                    final int totalCount = files.size();
                    HProgressRunnable rotateTask = new HProgressRunnableDialog(progressDialog) {

                        public void run() {
                            int count = 0;
                            dialog.printlnAction(I18N.translate("Rotate files"));
                            for (File file : files) {
                                if (stop) {
                                    break;
                                }
                                dialog.println(I18N.translate("Rotating") + " " + file);
                                try {
                                    rotateAccordingOrientation(file);
                                } catch (LLJTranException exc) {
                                    dialog.printlnError(exc.getMessage());
                                    logger.warning(exc.getMessage());
                                } catch (IOException exc) {
                                    dialog.printlnError(exc.getMessage());
                                    logger.warning(exc.getMessage());
                                }
                                count++;
                                dialog.setPercent((count * 100) / totalCount);
                            }
                            window.changer.topFrame.setCursor(Cursor.getDefaultCursor());
                            dialog.close();
                        }
                    };
                    progressDialog.setRunnable(rotateTask);
                    ThreadManager.addTask(rotateTask, "Rotating");
                    progressDialog.showDialog();
                }
                updateThumb();
                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_INFORMATION, I18N.translate("Successfully rotated {0} photos.", new Integer[] { files.size() }) + " " + I18N.translate("Other files are not photos or are without exif informations."));
            }
        });
        camSortButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Map<Date, File> filesSorted = new TreeMap<Date, File>();
                List<File> filesUnsorted = new ArrayList<File>();
                Date date;
                int count = 0;
                File file;
                for (int i = 0; i < filesListModel.size(); i++) {
                    file = (File) filesListModel.get(i);
                    if (Utils.isPhotoFile(file)) {
                        date = JPEG.getEXIFDate(JPEG.getEXIF(file));
                        if (date != null) {
                            while (filesSorted.containsKey(date)) {
                                date.setTime(date.getTime() + 1);
                            }
                            filesSorted.put(date, file);
                            count++;
                        } else {
                            filesUnsorted.add(file);
                        }
                    }
                }
                File[] filesUnsortedArray = filesUnsorted.toArray(new File[0]);
                Arrays.sort(filesUnsortedArray);
                filesListModel.removeAllElements();
                for (File file2 : filesSorted.values().toArray(new File[0])) {
                    filesListModel.addElement(file2);
                }
                for (File file3 : filesUnsortedArray) {
                    filesListModel.addElement(file3);
                }
                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_INFORMATION, I18N.translate("Successfully sorted {0} photos.", new Integer[] { count }) + " " + I18N.translate("Other files are not photos or are without exif informations."));
            }
        });
        camRenameButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] filesObj = filesListModel.toArray();
                File[] files = new File[filesObj.length];
                for (int i = 0; i < filesObj.length; i++) {
                    files[i] = (File) filesObj[i];
                }
                FileRenameDialog renameDialog = new FileRenameDialog(files, (String) albumCombo.getSelectedItem(), "jpg");
                if (renameDialog.getRenamedFiles() != null) {
                    filesListModel.removeAllElements();
                    for (File file : renameDialog.getRenamedFiles()) {
                        filesListModel.addElement(file);
                    }
                    cdWindow.cdTempRoastPanel.refreshDirectory();
                }
            }
        });
        albumCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String tempRoastDir = Config.getProperty(ConfigFile_v3.NODE_ROAST, ConfigFile_v3.NODE_ROAST_DIRECTORY);
                String albumSel = (String) albumCombo.getSelectedItem();
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    if ((albumSel == null) || (albumSel.equals(""))) {
                        new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Album name can't be empty.") + Config.LS + I18N.translate("You have to set new album name."));
                        return;
                    }
                    File newDir = new File(tempRoastDir, albumSel);
                    if (newDir.exists()) {
                        new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Album folder already exists. Specify another album name."));
                        return;
                    } else {
                        File oldDir = new File(tempRoastDir, editedAlbum);
                        logger.info("Rename folder from " + oldDir + " to " + newDir);
                        try {
                            if (!oldDir.renameTo(newDir)) {
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't rename album folder."));
                                return;
                            }
                        } catch (Exception exc) {
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't rename album folder."));
                            return;
                        }
                    }
                } else {
                    editedAlbum = (String) ((HComboBoxEditor) albumCombo.getEditor()).getOldItem();
                }
                loadAlbumFiles();
                cdWindow.cdTempRoastPanel.refreshDirectory();
            }
        });
        addAlbumButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String newAlbum = I18N.translate("NewAlbum");
                DefaultComboBoxModel model = (DefaultComboBoxModel) albumCombo.getModel();
                int i = 1;
                String newAlbumName = new String(newAlbum);
                while (model.getIndexOf(newAlbumName) != -1) {
                    i++;
                    newAlbumName = newAlbum + i;
                }
                String tempRoastDir = Config.getProperty(ConfigFile_v3.NODE_ROAST, ConfigFile_v3.NODE_ROAST_DIRECTORY);
                File newDir = new File(tempRoastDir, newAlbumName);
                try {
                    if (!newDir.mkdir()) {
                        new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't create album folder."));
                        return;
                    }
                } catch (Exception exc) {
                    new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't create album folder."));
                    return;
                }
                albumCombo.addItem(newAlbumName);
                albumCombo.setSelectedItem(newAlbumName);
                loadAlbumFiles();
                cdWindow.cdTempRoastPanel.refreshDirectory();
            }
        });
        rotateLeftButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] selection = filesList.getSelectedValues();
                if (selection.length == 1) {
                    File file = (File) selection[0];
                    try {
                        JPEG.transformJPEG(file, LLJTran.ROT_270, LLJTran.OPT_XFORM_APPX);
                    } catch (Exception exc) {
                        new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't rotate photo.") + "\n\n" + exc.getMessage());
                    }
                    updateThumb();
                }
            }
        });
        rotateRightButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] selection = filesList.getSelectedValues();
                if (selection.length == 1) {
                    File file = (File) selection[0];
                    try {
                        JPEG.transformJPEG(file, LLJTran.ROT_90, LLJTran.OPT_XFORM_APPX);
                    } catch (Exception exc) {
                        new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't rotate photo.") + "\n\n" + exc.getMessage());
                    }
                    updateThumb();
                }
            }
        });
        orientButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object[] selection = filesList.getSelectedValues();
                if (selection.length == 1) {
                    File file = (File) selection[0];
                    try {
                        JPEG.transformEXIF(file, Exif.ORIENTATION, orientCombo.getSelectedItem());
                    } catch (Exception exc) {
                        new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't change EXIF data.") + "\n\n" + exc.getMessage());
                    }
                    orientCombo.setSelectedItem(JPEG.getEXIFOrientation(JPEG.getEXIF(file)));
                    timeChooser.setDate(JPEG.getEXIFDate(JPEG.getEXIF(file)));
                }
            }
        });
        timeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                File file = (File) filesList.getSelectedValue();
                SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                try {
                    JPEG.transformEXIF(file, Exif.DATETIMEORIGINAL, df.format(timeChooser.getDate()));
                    timeChooser.setDate(JPEG.getEXIFDate(JPEG.getEXIF(file)));
                    orientCombo.setSelectedItem(JPEG.getEXIFOrientation(JPEG.getEXIF(file)));
                } catch (Exception exc) {
                    new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can't change EXIF data.") + "\n\n" + exc.getMessage());
                }
            }
        });
        shiftButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final Object[] files = filesList.getSelectedValues();
                HProgressDialog progressDialog = new HProgressDialog(window.changer.topFrame, I18N.translate("Time shift"), true, false);
                HProgressRunnable progressRunnable = new HProgressRunnableDialog(progressDialog) {

                    public void run() {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                        dialog.printlnAction(I18N.translate("Reading EXIF data"));
                        logger.info("exif data loading ...");
                        String errFiles = "";
                        String errMsg = "";
                        File file = null;
                        int count = 0;
                        try {
                            for (Object obj : files) {
                                file = (File) obj;
                                try {
                                    dialog.printItem(I18N.translate("Image") + " " + file.getName());
                                    Date date = JPEG.getEXIFDate(JPEG.getEXIF(file));
                                    if (date != null) {
                                        Calendar cal = I18N.getCalendar(date);
                                        cal.add(Calendar.DAY_OF_YEAR, (Integer) shiftDaySpinner.getValue());
                                        cal.add(Calendar.HOUR, (Integer) shiftHourSpinner.getValue());
                                        cal.add(Calendar.MINUTE, (Integer) shiftMinSpinner.getValue());
                                        cal.add(Calendar.SECOND, (Integer) shiftSecSpinner.getValue());
                                        JPEG.transformEXIF(file, Exif.DATETIMEORIGINAL, df.format(cal.getTime()));
                                        dialog.printlnItemOK();
                                    } else {
                                        dialog.printlnError(I18N.translate("Photo hasn't EXIF date."));
                                        errMsg += I18N.translate("Photo hasn't EXIF date.") + Config.LS;
                                        errFiles += ", " + file.getName();
                                        dialog.printlnItemBAD();
                                    }
                                    count++;
                                    dialog.setPercent((count * 100) / files.length);
                                } catch (Exception exc) {
                                    dialog.printlnItemBAD();
                                    if (exc.getMessage() != null) {
                                        dialog.printlnError(exc.getMessage());
                                    }
                                    errMsg += exc.getMessage() + Config.LS;
                                    errFiles += ", " + file.getName();
                                }
                                if (stop) {
                                    throw new HException(I18N.translate("Operation was canceled."));
                                }
                            }
                            timeChooser.setDate(JPEG.getEXIFDate(JPEG.getEXIF(file)));
                        } catch (HException exc) {
                            dialog.printlnError(exc.getMessage());
                            new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_WARNING, exc.getMessage());
                        } finally {
                            dialog.close();
                            if (!errFiles.equals("")) {
                                errFiles = errFiles.substring(2);
                                new HMessageDialog(window.changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Time was not shifted in these files.") + Config.LS + errFiles + Config.LS + Config.LS + errMsg);
                            }
                        }
                    }
                };
                progressDialog.setRunnable(progressRunnable);
                ThreadManager.addTask(progressRunnable, "EXIF shifting");
                if (files.length > 10) {
                    progressDialog.showDialog();
                }
            }
        });
    }
