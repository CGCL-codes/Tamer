    public BookmarkTab(MainForm mf) {
        super(new FormLayout("f:p:g, f:p", "f:p, f:p, f:p:g"));
        this.mainForm = mf;
        CellConstraints cc = new CellConstraints();
        add(changeBookmarks = new JCheckBox("Change chapter bookmarks"), cc.xy(1, 1));
        changeBookmarks.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateEnabledState();
            }
        });
        add(load = new JButton("Load from document"), cc.xy(2, 1));
        load.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                List<PdfBookmark> bm = mainForm.getInputTab().loadBookmarks();
                bookmarks.clear();
                appendBookmarks(bm);
            }
        });
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.add(importPDF = new JButton("Import from PDF"));
        importPDF.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = mainForm.getPdfChooser();
                if (chooser.showOpenDialog(mainForm) == JFileChooser.APPROVE_OPTION) {
                    try {
                        PdfInputFile pif = new PdfInputFile(chooser.getSelectedFile(), "");
                        bookmarks.clear();
                        appendBookmarks(pif.getBookmarks(1));
                        pif.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(mainForm, ex.getMessage(), "Error reading file", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(importCSV = new JButton("Import from CSV"));
        importCSV.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                if (jfc.showOpenDialog(mainForm) == JFileChooser.APPROVE_OPTION) {
                    importCSV(jfc.getSelectedFile());
                }
            }
        });
        panel.add(exportCSV = new JButton("Export to CSV"));
        exportCSV.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                if (jfc.showSaveDialog(mainForm) == JFileChooser.APPROVE_OPTION) {
                    File f = jfc.getSelectedFile();
                    if (f.exists()) {
                        if (JOptionPane.showConfirmDialog(mainForm, "Overwrite existing file?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return;
                    }
                    exportCSV(f);
                }
            }
        });
        add(panel, cc.xyw(1, 2, 2));
        add(bookmarks = new TableComponent(new String[] { "Depth", "Open", "Title", "Page", "Position", "Bold", "Italic", "Options" }, new Class[] { Integer.class, Boolean.class, String.class, Integer.class, String.class, Boolean.class, Boolean.class, String.class }, new Object[] { 1, false, "", 1, "", false, false, "" }), cc.xyw(1, 3, 2));
        updateEnabledState();
    }
