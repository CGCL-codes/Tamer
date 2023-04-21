    private JMenuBar createMenuBar() {
        JMenuItem jmItem;
        JMenuBar menuBar;
        JMenu currentMenuBar;
        menuBar = new JMenuBar();
        currentMenuBar = new JMenu("File");
        menuBar.add(currentMenuBar);
        jmItem = new JMenuItem("Reset Graph");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Main.resetGraph();
            }
        });
        currentMenuBar.add(jmItem);
        jmItem = new JMenuItem("Open");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Calculator Files", "dat"));
                if (chooser.showOpenDialog(GraphFrame.this) == JFileChooser.APPROVE_OPTION) {
                    String file = chooser.getSelectedFile().getPath();
                    if (file.substring(file.lastIndexOf(".") + 1).equals("dat")) {
                        try {
                            loadGraph(chooser.getSelectedFile().getPath());
                        } catch (FileNotFoundException e1) {
                        } catch (IOException e1) {
                        } catch (ClassNotFoundException e1) {
                        }
                    }
                }
                repaint();
            }
        });
        currentMenuBar.add(jmItem);
        currentMenuBar.addSeparator();
        jmItem = new JMenuItem("Save As...");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Calculator Files", "dat"));
                if (chooser.showSaveDialog(GraphFrame.this) == JFileChooser.APPROVE_OPTION) {
                    String file = chooser.getSelectedFile().getPath();
                    if (file.lastIndexOf(".") < 0) file += ".dat";
                    if (file.substring(file.lastIndexOf(".") + 1).equals("dat")) {
                        try {
                            File f = new File(file);
                            if (!f.exists() || JOptionPane.showConfirmDialog(GraphFrame.this, "There is already a file with the name " + file.substring(file.lastIndexOf("\\") + 1) + ", are you sure you wish to overwrite it?") == JOptionPane.YES_OPTION) {
                                saveGraph(file);
                            }
                        } catch (HeadlessException e1) {
                        } catch (FileNotFoundException e1) {
                        } catch (IOException e1) {
                        }
                    }
                }
            }
        });
        currentMenuBar.add(jmItem);
        currentMenuBar.addSeparator();
        jmItem = new JMenuItem("Open Calculator");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Main.showCalc();
            }
        });
        currentMenuBar.add(jmItem);
        jmItem = new JMenuItem("Exit");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                dispose();
            }
        });
        currentMenuBar.add(jmItem);
        jmItem = new JMenuItem("Exit All");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        currentMenuBar.add(jmItem);
        currentMenuBar = new JMenu("Tools");
        menuBar.add(currentMenuBar);
        jmItem = new JMenuItem("Find Function Values");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(true);
                dialog.setLocation(GraphFrame.this.getLocationOnScreen().x + (GraphFrame.this.getWidth() / 2) - (dialog.getWidth() / 2), GraphFrame.this.getLocationOnScreen().y + (GraphFrame.this.getHeight() / 2) - (dialog.getHeight() / 2));
            }
        });
        currentMenuBar.add(jmItem);
        currentMenuBar.addSeparator();
        jmItem = new JMenuItem("Settings");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Main.showSettings();
            }
        });
        currentMenuBar.add(jmItem);
        currentMenuBar = new JMenu("Help");
        menuBar.add(currentMenuBar);
        jmItem = new JMenuItem("Help Contents");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Main.showHelp();
            }
        });
        currentMenuBar.add(jmItem);
        currentMenuBar.addSeparator();
        jmItem = new JMenuItem("About BeastCalc");
        jmItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Main.showAbout();
            }
        });
        currentMenuBar.add(jmItem);
        return menuBar;
    }
