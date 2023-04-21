    public MainWindow(Controller c, Map namedLayers) {
        super();
        controller = c;
        openLayers.putAll(namedLayers);
        Container cpane = getContentPane();
        cpane.setLayout(new BorderLayout());
        cpane.add("South", status = new JLabel());
        cpane.add("Center", new JScrollPane(jt = new JTable(ofm = new OpenFileModel())));
        refreshStatus();
        JMenuBar mb = new JMenuBar();
        JMenu mm;
        JMenuItem mi;
        mb.add(mm = new JMenu("Store"));
        mm.setMnemonic(KeyEvent.VK_S);
        mm.add(mi = new JMenuItem("Select store directory..."));
        mi.setMnemonic(KeyEvent.VK_S);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                File sdir = selectDir("Select store directory", controller.getDirectory());
                if (sdir != null) {
                    showWaitCursor();
                    controller.setDirectory(sdir);
                    if (controller.canBlockCountBeSet()) {
                        askForBlockCount();
                    }
                    refreshStatus();
                    showNormalCursor();
                }
            }
        });
        mm.add(mi = new JMenuItem("Import from Local store..."));
        mi.setMnemonic(KeyEvent.VK_L);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                File sdir = selectDir("Import From", controller.getDirectory());
                if (sdir != null) {
                    try {
                        importFrom(sdir.toURI().toURL());
                    } catch (MalformedURLException ex) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Incorrect Store URL", "Import Layer", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        mm.add(mi = new JMenuItem("Import from Remote store (URL)..."));
        mi.setMnemonic(KeyEvent.VK_U);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String urlS = JOptionPane.showInputDialog(MainWindow.this, "URL:", "Import Layer", JOptionPane.QUESTION_MESSAGE);
                if (urlS != null) {
                    try {
                        importFrom(new URL(urlS));
                    } catch (MalformedURLException ex) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Incorrect Store URL", "Import Layer", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        mm.addSeparator();
        mm.add(mi = new JMenuItem("Exit"));
        mi.setMnemonic(KeyEvent.VK_X);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        mb.add(mm = new JMenu("Layer"));
        mm.setMnemonic(KeyEvent.VK_L);
        mm.add(mi = new JMenuItem("Open..."));
        mi.setMnemonic(KeyEvent.VK_O);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String pw = readPassword("Open Layer (Name: " + layerindex + ")");
                if (pw != null) {
                    showWaitCursor();
                    controller.open(pw);
                    openLayers.put(pw, "" + (layerindex++));
                    refreshStatus();
                    showNormalCursor();
                }
            }
        });
        mm.add(mi = new JMenuItem("Close"));
        mi.setMnemonic(KeyEvent.VK_L);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String[] pwds = getSelectedLayers();
                showWaitCursor();
                for (int i = 0; i < pwds.length; i++) {
                    controller.close(pwds[i]);
                }
                refreshStatus();
                showNormalCursor();
            }
        });
        mm.addSeparator();
        mm.add(mi = new JMenuItem("Create..."));
        mi.setMnemonic(KeyEvent.VK_C);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String pw = readPassword("Create Layer (Name: " + layerindex + ")");
                if (pw != null) {
                    if (pw.length() == 0) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Password must be  at least " + "one character long!", "Create Layer", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        int count = Integer.parseInt(JOptionPane.showInputDialog(MainWindow.this, "How many blocks?", "Create Layer", JOptionPane.QUESTION_MESSAGE));
                        if (count <= 0) throw new NumberFormatException();
                        showWaitCursor();
                        if (controller.create(pw, count)) {
                            controller.open(pw);
                            openLayers.put(pw, "" + (layerindex++));
                            refreshStatus();
                        } else {
                            JOptionPane.showMessageDialog(MainWindow.this, "Could not create layer!", "Create Layer", JOptionPane.ERROR_MESSAGE);
                        }
                        showNormalCursor();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Incorrect number!", "Create Layer", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        mm.add(mi = new JMenuItem("Create Garbage..."));
        mi.setMnemonic(KeyEvent.VK_G);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    int count = Integer.parseInt(JOptionPane.showInputDialog(MainWindow.this, "How many blocks?", "Create Layer", JOptionPane.QUESTION_MESSAGE));
                    if (count <= 0) throw new NumberFormatException();
                    showWaitCursor();
                    if (controller.create("", count)) {
                        refreshStatus();
                    } else {
                        JOptionPane.showMessageDialog(MainWindow.this, "Could not create layer!", "Create Layer", JOptionPane.ERROR_MESSAGE);
                    }
                    showNormalCursor();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Incorrect number!", "Create Layer", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mm.add(mi = new JMenuItem("Delete..."));
        mi.setMnemonic(KeyEvent.VK_D);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String[] pwds = getSelectedLayers();
                int res = JOptionPane.showConfirmDialog(MainWindow.this, "Really delete these " + pwds.length + " Layers?", "Delete Layers", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                    showWaitCursor();
                    for (int i = 0; i < pwds.length; i++) {
                        controller.close(pwds[i]);
                        controller.delete(pwds[i]);
                    }
                    refreshStatus();
                    showNormalCursor();
                }
            }
        });
        mm.add(mi = new JMenuItem("Delete all closed..."));
        mi.setMnemonic(KeyEvent.VK_A);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int res = JOptionPane.showConfirmDialog(MainWindow.this, "Really delete all closed Layers?", "Delete Layers", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res == JOptionPane.YES_OPTION) {
                    showWaitCursor();
                    controller.deleteClosed();
                    refreshStatus();
                    showNormalCursor();
                }
            }
        });
        mb.add(mm = new JMenu("File"));
        mm.setMnemonic(KeyEvent.VK_F);
        mm.add(mi = new JMenuItem("Insert..."));
        mi.setMnemonic(KeyEvent.VK_I);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int[] ind = jt.getSelectedRows();
                if (ind.length != 1 || ofm.getFile(ind[0]).getContentName() != null) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Select an empty slot", "Insert File", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setMultiSelectionEnabled(false);
                jfc.setDialogTitle("Insert File");
                if (jfc.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                    if (jfc.getSelectedFile().exists()) {
                        showWaitCursor();
                        controller.insert(jfc.getSelectedFile(), ofm.getFile(ind[0]));
                        refreshStatus();
                        showNormalCursor();
                    }
                }
            }
        });
        mm.add(mi = new JMenuItem("Save as..."));
        mi.setMnemonic(KeyEvent.VK_S);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int[] ind = jt.getSelectedRows();
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setMultiSelectionEnabled(false);
                jfc.setDialogTitle("Save as");
                for (int i = 0; i < ind.length; i++) {
                    OpenFile of = ofm.getFile(ind[i]);
                    if (of.getContentName() != null) {
                        jfc.setSelectedFile(new File(jfc.getCurrentDirectory(), of.getContentName()));
                        if (jfc.showSaveDialog(MainWindow.this) != JFileChooser.APPROVE_OPTION) {
                            return;
                        }
                        if (jfc.getSelectedFile().exists()) {
                            int res = JOptionPane.showConfirmDialog(MainWindow.this, "Really overwrite?", "File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (res != JOptionPane.YES_OPTION) {
                                continue;
                            }
                        }
                        showWaitCursor();
                        controller.fetch(of, jfc.getSelectedFile());
                        showNormalCursor();
                    }
                }
            }
        });
        mm.add(mi = new JMenuItem("Delete..."));
        mi.setMnemonic(KeyEvent.VK_D);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int res = JOptionPane.showConfirmDialog(MainWindow.this, "Really delete all selected files?", "Delete Files", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (res != JOptionPane.YES_OPTION) {
                    return;
                }
                int[] ind = jt.getSelectedRows();
                showWaitCursor();
                for (int i = 0; i < ind.length; i++) {
                    OpenFile of = ofm.getFile(ind[i]);
                    if (of.getContentName() != null) {
                        controller.delete(of);
                    }
                }
                refreshStatus();
                showNormalCursor();
            }
        });
        mb.add(mm = new JMenu("Filesystem"));
        mm.setMnemonic(KeyEvent.VK_S);
        mm.add(mi = new JMenuItem("Access 10% of all files"));
        mi.setMnemonic(KeyEvent.VK_C);
        mi.addActionListener(new TouchActionListener(10, Controller.TOUCH_MODE_ACCESS));
        mm.add(mi = new JMenuItem("Modify 10% of all files"));
        mi.setMnemonic(KeyEvent.VK_O);
        mi.addActionListener(new TouchActionListener(10, Controller.TOUCH_MODE_MODIFY));
        mm.add(mi = new JMenuItem("Recreate 10% of all files"));
        mi.setMnemonic(KeyEvent.VK_E);
        mi.addActionListener(new TouchActionListener(10, Controller.TOUCH_MODE_RECREATE));
        mm.add(mi = new JMenuItem("Double-copy 10% of all files"));
        mi.setMnemonic(KeyEvent.VK_U);
        mi.addActionListener(new TouchActionListener(10, Controller.TOUCH_MODE_COPY_TWICE));
        mm.addSeparator();
        mm.add(mi = new JMenuItem("Access all files"));
        mi.setMnemonic(KeyEvent.VK_A);
        mi.addActionListener(new TouchActionListener(100, Controller.TOUCH_MODE_ACCESS));
        mm.add(mi = new JMenuItem("Modify all files"));
        mi.setMnemonic(KeyEvent.VK_M);
        mi.addActionListener(new TouchActionListener(100, Controller.TOUCH_MODE_MODIFY));
        mm.add(mi = new JMenuItem("Recreate all files"));
        mi.setMnemonic(KeyEvent.VK_R);
        mi.addActionListener(new TouchActionListener(100, Controller.TOUCH_MODE_RECREATE));
        mm.add(mi = new JMenuItem("Double-copy all files"));
        mi.setMnemonic(KeyEvent.VK_D);
        mi.addActionListener(new TouchActionListener(100, Controller.TOUCH_MODE_COPY_TWICE));
        getRootPane().setJMenuBar(mb);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        if (controller.canBlockCountBeSet()) {
            askForBlockCount();
        }
    }
