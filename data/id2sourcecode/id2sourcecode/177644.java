    private void run() throws Exception {
        frame = new JFrame("Flying Saucer: Show Font Glyphs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel optionsPanel = new JPanel(new BorderLayout());
        fontPathTF = new JTextField();
        fontPathTF.setColumns(40);
        familyNameField = new JTextField();
        familyNameField.setEnabled(true);
        familyNameField.setEditable(false);
        familyNameField.setColumns(40);
        JPanel top1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top1.add(new JLabel("Enter font path: "));
        top1.add(fontPathTF);
        JButton chooseFontFileBtn = new JButton("...");
        chooseFontFileBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String filename = File.separator + "tmp";
                String famPath = fontPathTF.getText();
                if (currentFont != null && famPath.length() > 0) {
                    filename = new File(famPath).getParent();
                }
                JFileChooser fc = new JFileChooser(new File(filename));
                fc.showOpenDialog(frame);
                File selFile = fc.getSelectedFile();
                Font font = null;
                String msg = "";
                try {
                    font = loadFont(selFile.getPath());
                } catch (IOException e1) {
                    msg = e1.getMessage();
                }
                if (font == null) {
                    JOptionPane.showMessageDialog(frame, "Can't load file--is it a valid Font file? " + msg);
                } else {
                    fontPathTF.setText(selFile.getPath());
                    familyNameField.setText(font.getFamily());
                }
            }
        });
        top1.add(chooseFontFileBtn);
        ActionListener outputSelection = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                outputType = e.getActionCommand();
                enableButtons();
                if (currentFont != null) {
                    deferredChangePage(curFrom);
                }
            }
        };
        JRadioButton jrbCodePoint = new JRadioButton("Codepoints");
        jrbCodePoint.setActionCommand(OUTPUT_CODEPOINTS);
        jrbCodePoint.addActionListener(outputSelection);
        jrbCodePoint.setSelected(true);
        JRadioButton jrbEntities = new JRadioButton("Entities");
        jrbEntities.setActionCommand(OUTPUT_ENTITIES);
        jrbEntities.addActionListener(outputSelection);
        ButtonGroup bg = new ButtonGroup();
        bg.add(jrbCodePoint);
        bg.add(jrbEntities);
        top1.add(jrbCodePoint);
        top1.add(jrbEntities);
        JPanel top2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top2.add(new JLabel("Family: "));
        top2.add(familyNameField);
        JPanel top = new JPanel(new BorderLayout());
        top.add(top1, BorderLayout.NORTH);
        top.add(top2, BorderLayout.CENTER);
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prevBtn = new JButton("Prev");
        nextBtn = new JButton("Next");
        JButton pdfBtn = new JButton("PDF");
        JButton renderBtn = new JButton("Render");
        prevBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                deferredChangePage(curFrom - ENT_PER_PAGE);
            }
        });
        nextBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                deferredChangePage(curFrom + ENT_PER_PAGE);
            }
        });
        renderBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                deferredChangePage(curFrom);
            }
        });
        pdfBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                resolveCurrentFont();
                if (currentFont == null) {
                    JOptionPane.showMessageDialog(frame, "Need a valid font file path");
                    fontPathTF.requestFocus();
                    return;
                }
                deferredLoadAndRender(curFrom, TO_PDF);
            }
        });
        mid.add(prevBtn);
        mid.add(nextBtn);
        mid.add(renderBtn);
        mid.add(pdfBtn);
        optionsPanel.add(top, BorderLayout.NORTH);
        optionsPanel.add(mid, BorderLayout.CENTER);
        fontPathTF.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                deferredChangePage(curFrom);
            }
        });
        xpanel = new XHTMLPanel();
        xpanel.addDocumentListener(new DefaultDocumentListener() {

            public void documentLoaded() {
                frame.setCursor(Cursor.getDefaultCursor());
            }
        });
        resetMouseListeners();
        FSScrollPane scroll = new FSScrollPane(xpanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel cont = new JPanel(new BorderLayout());
        cont.add(optionsPanel, BorderLayout.NORTH);
        cont.add(scroll, BorderLayout.CENTER);
        frame.getContentPane().add(cont);
        frame.pack();
        frame.setSize(1024, 730);
        enableButtons();
        frame.setVisible(true);
    }
