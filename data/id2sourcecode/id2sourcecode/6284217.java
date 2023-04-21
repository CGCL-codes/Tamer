    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createCompoundBorder(new BevelBorder(BevelBorder.RAISED), new TitledBorder("Select necessary Configuration")));
        this.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setFocusable(false);
        description.setLineWrap(false);
        description.setText("Please select the iTunes directory and the directory, where you store your MP3-files");
        description.setFont(new JLabel().getFont().deriveFont(description.getFont().getStyle(), 14));
        description.setWrapStyleWord(true);
        this.add(description, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Select your iTunes Library"), gbc);
        gbc.weightx = 0;
        gbc.gridx = 1;
        JButton chooseiTunesDir = new JButton("...");
        final JTextField iTunesDirectoryPath = new JTextField();
        iTunesDirectoryPath.setPreferredSize(new Dimension(300, iTunesDirectoryPath.getPreferredSize().height));
        if (iTunesLibrary != null) {
            iTunesDirectoryPath.setText(iTunesLibrary.getAbsolutePath());
        }
        chooseiTunesDir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Select iTunes Library");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        if ("iTunes Library.itl".equalsIgnoreCase(f.getName())) {
                            return true;
                        }
                        if (f.isDirectory()) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "iTunes Library.itl";
                    }
                });
                if (iTunesLibrary != null) {
                    chooser.setCurrentDirectory(iTunesLibrary.getParentFile());
                }
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    iTunesLibrary = chooser.getSelectedFile();
                    iTunesDirectoryPath.setText(iTunesLibrary.getAbsolutePath());
                    iTunesDirectoryPath.setCaretPosition(0);
                } else {
                    iTunesLibrary = null;
                    iTunesDirectoryPath.setText("");
                }
                storeProperties();
                updateUIElements();
            }
        });
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(chooseiTunesDir, gbc);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iTunesDirectoryPath.setEditable(false);
        iTunesDirectoryPath.setFocusable(false);
        this.add(iTunesDirectoryPath, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Select your Music Directory"), gbc);
        gbc.weightx = 0;
        gbc.gridx = 1;
        JButton chooseMusicDir = new JButton("...");
        final JTextField musicDirectoryPath = new JTextField();
        musicDirectoryPath.setPreferredSize(new Dimension(300, musicDirectoryPath.getPreferredSize().height));
        if (musicDirectory != null) {
            musicDirectoryPath.setText(musicDirectory.getAbsolutePath());
        }
        chooseMusicDir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Select your music directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (musicDirectory != null && musicDirectory.exists() && musicDirectory.isDirectory()) {
                    chooser.setCurrentDirectory(musicDirectory);
                }
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    musicDirectory = chooser.getSelectedFile();
                    musicDirectoryPath.setText(musicDirectory.getAbsolutePath());
                    musicDirectoryPath.setCaretPosition(0);
                } else {
                    musicDirectory = null;
                    musicDirectoryPath.setText("");
                }
                storeProperties();
                updateUIElements();
            }
        });
        gbc.anchor = GridBagConstraints.LINE_END;
        this.add(chooseMusicDir, gbc);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 2;
        musicDirectoryPath.setEditable(false);
        musicDirectoryPath.setFocusable(false);
        this.add(musicDirectoryPath, gbc);
        run = new JButton("REPAIR");
        run.setEnabled(false);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        run.setEnabled(false);
                        Repair r = new Repair();
                        try {
                            r.repair(musicDirectory, iTunesLibrary, new IProgressMonitor() {

                                @Override
                                public void done() {
                                }

                                @Override
                                public int getProgress() {
                                    return 0;
                                }

                                @Override
                                public boolean isCanceled() {
                                    return false;
                                }

                                @Override
                                public void setCanceled(boolean c) {
                                }

                                @Override
                                public void setProgress(int p) {
                                }

                                @Override
                                public void setTaskName(final String name) {
                                    appendStatusText(name);
                                }

                                @Override
                                public void setTaskName(String name, int level) {
                                    setTaskName(name);
                                }

                                @Override
                                public void worked() {
                                }
                            });
                        } catch (Throwable t) {
                            appendStatusText("\n" + StackTraceUtil.getCustomStackTrace(t));
                            JOptionPane.showMessageDialog(GuiPanel.this, t.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        run.setEnabled(true);
                    }
                }).start();
            }
        });
        gbc.weighty = 1;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(run, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridy++;
        this.add(new JLabel(), gbc);
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_END;
        JPanel info = initCopyrightInfo();
        info.setBorder(BorderFactory.createTitledBorder("Info"));
        this.add(info, gbc);
        gbc.gridy++;
        status = new JTextArea();
        status.setEditable(false);
        status.setText("Welcome to iTunes Library Repair\nVisit http://sourceforge.net/projects/itunesrepair/\n\n\n");
        status.setBackground(Color.BLACK);
        status.setForeground(Color.GREEN);
        status.setBorder(BorderFactory.createTitledBorder("Status"));
        status.setLineWrap(true);
        status.setWrapStyleWord(true);
        final JPopupMenu statusContextMenu = new JPopupMenu("Clear");
        final JMenuItem statusContextMenuClear = new JMenuItem("Clear Status Panel");
        statusContextMenuClear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        status.setText("");
                    }
                });
            }
        });
        final JMenuItem statusContextMenuCopy = new JMenuItem("Copy selected Text");
        statusContextMenuCopy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                status.copy();
            }
        });
        statusContextMenu.add(statusContextMenuCopy);
        statusContextMenu.add(statusContextMenuClear);
        status.setComponentPopupMenu(statusContextMenu);
        JScrollPane statusScroll = new JScrollPane(status);
        statusScroll.setMinimumSize(statusScroll.getPreferredSize());
        gbc.weighty = 0;
        gbc.weightx = 1;
        this.add(statusScroll, gbc);
    }
