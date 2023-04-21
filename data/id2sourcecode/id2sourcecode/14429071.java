    public Capture(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        rectScreenSize = new Rectangle(dimScreenSize);
        final JFileChooser fcSave = new JFileChooser();
        fcSave.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fcSave.setAcceptAllFileFilterUsed(false);
        fcSave.setFileFilter(new ImageFileFilter());
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("File");
        ActionListener al;
        JMenuItem mi = new JMenuItem("Save As...");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ia.getImage() == null) {
                    showError("No captured image.");
                    return;
                }
                fcSave.setSelectedFile(null);
                if (fcSave.showSaveDialog(Capture.this) != JFileChooser.APPROVE_OPTION) return;
                File file = fcSave.getSelectedFile();
                String path = file.getAbsolutePath().toLowerCase();
                if (!path.endsWith(".jpg") && !path.endsWith(".jpeg")) file = new File(path += ".jpg");
                if (file.exists()) {
                    int choice = JOptionPane.showConfirmDialog(null, "Overwrite file?", "Capture", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.NO_OPTION) return;
                }
                ImageWriter writer = null;
                ImageOutputStream ios = null;
                try {
                    Iterator iter;
                    iter = ImageIO.getImageWritersByFormatName("jpeg");
                    if (!iter.hasNext()) {
                        showError("Unable to save image to jpeg file type.");
                        return;
                    }
                    writer = (ImageWriter) iter.next();
                    ios = ImageIO.createImageOutputStream(file);
                    writer.setOutput(ios);
                    ImageWriteParam iwp = writer.getDefaultWriteParam();
                    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwp.setCompressionQuality(0.95f);
                    writer.write(null, new IIOImage((BufferedImage) ia.getImage(), null, null), iwp);
                } catch (IOException e2) {
                    showError(e2.getMessage());
                } finally {
                    try {
                        if (ios != null) {
                            ios.flush();
                            ios.close();
                        }
                        if (writer != null) writer.dispose();
                    } catch (IOException e2) {
                    }
                }
            }
        };
        mi.addActionListener(al);
        menu.add(mi);
        menu.addSeparator();
        mi = new JMenuItem("Exit");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(mi);
        mb.add(menu);
        menu = new JMenu("Capture");
        mi = new JMenuItem("Capture");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                BufferedImage biScreen;
                biScreen = robot.createScreenCapture(rectScreenSize);
                setVisible(true);
                ia.setImage(biScreen);
                jsp.getHorizontalScrollBar().setValue(0);
                jsp.getVerticalScrollBar().setValue(0);
            }
        };
        mi.addActionListener(al);
        menu.add(mi);
        mb.add(menu);
        mi = new JMenuItem("Crop");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.ALT_MASK));
        al = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ia.crop()) {
                    jsp.getHorizontalScrollBar().setValue(0);
                    jsp.getVerticalScrollBar().setValue(0);
                } else showError("Out of bounds.");
            }
        };
        mi.addActionListener(al);
        menu.add(mi);
        mb.add(menu);
        setJMenuBar(mb);
        getContentPane().add(jsp = new JScrollPane(ia));
        setSize(dimScreenSize.width / 2, dimScreenSize.height / 2);
        setLocation((dimScreenSize.width - dimScreenSize.width / 2) / 2, (dimScreenSize.height - dimScreenSize.height / 2) / 2);
        setVisible(true);
    }
