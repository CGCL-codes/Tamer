    private void buildGui() {
        m_dlgAvi = new JDialog(m_owner, "Create Avi");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        m_dlgAvi.setLocation((int) (dim.getWidth() / 2.0), (int) (dim.getHeight() / 2.0));
        m_btCopyTo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ImagePlus ip = null;
                JFileChooser fs = new JFileChooser(OpenDialog.getLastDirectory());
                fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fs.showSaveDialog(m_dlgAvi);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (m_nFirstIdx != -1 && m_nLastIdx != -1) ip = createNewImagePlus(m_imagePlus, m_nFirstIdx, m_nLastIdx); else ip = m_imagePlus;
                    IJTools.saveImageStack(ip, fs.getSelectedFile().getAbsolutePath());
                }
            }
        });
        m_btCreateAvi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    ImagePlus ip = null;
                    JFileChooser fs = new JFileChooser(OpenDialog.getLastDirectory());
                    fs.setFileFilter(new FileFilter() {

                        @Override
                        public boolean accept(File f) {
                            return (f.getName().toLowerCase().endsWith(".avi") || f.isDirectory());
                        }

                        @Override
                        public String getDescription() {
                            return "AVI files";
                        }
                    });
                    int returnVal = fs.showSaveDialog(m_dlgAvi);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File f = ensureCorrectFileExtension(fs.getSelectedFile());
                        if (m_nFirstIdx != -1 && m_nLastIdx != -1) ip = createNewImagePlus(m_imagePlus, m_nFirstIdx, m_nLastIdx); else ip = m_imagePlus;
                        if (generateInfoFile(m_imagePlus, f, m_nFirstIdx, m_nLastIdx)) {
                            m_aviWriter.writeImage(ip, f.getAbsolutePath(), AVI_Writer.NO_COMPRESSION, 0);
                            JOptionPane.showMessageDialog(m_dlgAvi, "AVI creation succesful", "Success", JOptionPane.PLAIN_MESSAGE);
                            m_dlgAvi.dispose();
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(m_dlgAvi, "An error occurred during the AVI creation", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        m_tfFirstImageName.setEditable(false);
        m_btFrom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_nFirstIdx = m_imagePlus.getCurrentSlice();
                m_tfFirstImageName.setText(generateNameText(m_imagePlus, m_nFirstIdx));
                m_btGoToFirst.setEnabled(true);
            }
        });
        m_tfLastImageName.setEditable(false);
        m_btTo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_nLastIdx = m_imagePlus.getCurrentSlice();
                m_tfLastImageName.setText(generateNameText(m_imagePlus, m_nLastIdx));
                m_btGoToLast.setEnabled(true);
            }
        });
        m_btGoToFirst.setEnabled(false);
        m_btGoToFirst.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_nFirstIdx != -1) m_imagePlus.setSlice(m_nFirstIdx);
            }
        });
        m_btGoToLast.setEnabled(false);
        m_btGoToLast.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (m_nFirstIdx != -1) m_imagePlus.setSlice(m_nLastIdx);
            }
        });
        JPanel pnButtons = new JPanel();
        pnButtons.add(m_btCopyTo);
        pnButtons.add(m_btCreateAvi);
        JPanel pnBase = new JPanel(new MigLayout("", "[grow, fill]", "[]5[]10[]"));
        pnBase.add(new JLabel("First image: "));
        pnBase.add(m_tfFirstImageName);
        pnBase.add(m_btFrom, "");
        pnBase.add(m_btGoToFirst, "wrap");
        pnBase.add(new JLabel("Last image: "));
        pnBase.add(m_tfLastImageName);
        pnBase.add(m_btTo, "");
        pnBase.add(m_btGoToLast, "wrap");
        pnBase.add(pnButtons, "span, alignx center, grow 0");
        m_dlgAvi.getContentPane().add(pnBase);
        m_dlgAvi.pack();
        m_dlgAvi.setVisible(true);
    }
