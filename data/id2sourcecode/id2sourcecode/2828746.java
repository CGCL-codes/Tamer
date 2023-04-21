    public static void createUserInterface() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        WindowListener windowListener = new WindowAdapter() {

            public void windowClosing(WindowEvent w) {
                WorkDispatcher.shutdownNow();
                frame.setVisible(false);
                frame.dispose();
            }
        };
        frame.addWindowListener(windowListener);
        menubar = new JMenuBar();
        frame.setIconImage(IMAGE_ICON.getImage());
        frame.setJMenuBar(menubar);
        frame.getContentPane().setLayout(new BorderLayout());
        p = new JPanel() {

            static final long serialVersionUID = 0;

            public void paint(Graphics g) {
                super.paint(g);
                BufferedImage[] results = WorkDispatcher.retrieveGraphicsResults();
                if (results != null) {
                    g.drawImage(results[0], 0, 0, null);
                    g.drawImage(results[1], results[1].getWidth(), 0, null);
                    if (OptionsObject.getFpsCounter()) {
                        g.setColor(Color.WHITE);
                        g.drawString("FPS: " + fps, 20, 20);
                    }
                } else if (specLogo != null) {
                    g.drawImage(specLogo, 0, 0, null);
                }
            }
        };
        frame.getContentPane().add(p, BorderLayout.CENTER);
        statusBox = new JTextField(DEFAULT_STATUS_MESSAGE);
        statusBox.setEditable(false);
        convertButton = new JButton("Convert");
        bottomPane = new JPanel();
        bottomPane.setLayout(new GridLayout(2, 1));
        bottomPane.add(statusBox);
        bottomPane.add(convertButton);
        frame.getContentPane().add(bottomPane, BorderLayout.PAGE_END);
        JMenu fileMenu = new JMenu("File");
        folder = new JMenuItem("Choose input files");
        folder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JFileChooser jfc = new JFileChooser() {

                    static final long serialVersionUID = 1L;

                    public void approveSelection() {
                        for (File f : this.getSelectedFiles()) {
                            if (f.isDirectory()) {
                                return;
                            }
                        }
                        super.approveSelection();
                    }
                };
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (inFiles != null && inFiles.length > 0) {
                    jfc.setCurrentDirectory(inFiles[0].getParentFile());
                }
                jfc.setAcceptAllFileFilterUsed(false);
                jfc.setFileFilter(new FileFilter() {

                    public String getDescription() {
                        return "AVI, MOV Videos, JPG, PNG and GIF Images";
                    }

                    public boolean accept(File f) {
                        String name = f.getAbsolutePath().toLowerCase();
                        return (f.isDirectory() || name.endsWith(".gif") || name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".avi") || name.endsWith(".mov") && !name.contains(SaveHelper.FILE_SUFFIX));
                    }
                });
                jfc.setMultiSelectionEnabled(true);
                if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(null)) {
                    inFiles = jfc.getSelectedFiles();
                    processPreview();
                }
            }
        });
        outputFolder = new JMenuItem("Choose output folder");
        outputFolder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JFileChooser jfc = new JFileChooser("Choose output directory");
                if (outFolder != null) {
                    jfc.setCurrentDirectory(outFolder);
                }
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(null)) {
                    outFolder = jfc.getSelectedFile();
                }
            }
        });
        convertButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (inFiles == null || inFiles.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please choose the input images first", "Files not selected", JOptionPane.OK_OPTION);
                    return;
                }
                if (outFolder == null) {
                    JOptionPane.showMessageDialog(null, "Please choose a folder for outputing images first", "Folder not selected", JOptionPane.OK_OPTION);
                    return;
                }
                try {
                    startFpsCalculator();
                    processFiles();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error has occurred: " + e.getMessage(), "Guru meditation", JOptionPane.OK_OPTION);
                }
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
                System.exit(0);
            }
        });
        fileMenu.add(folder);
        fileMenu.add(outputFolder);
        fileMenu.add(new Separator());
        fileMenu.add(exit);
        menubar.add(fileMenu);
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem preferencesItem = new JMenuItem("Preferences");
        preferencesItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (!preferences.isShowing()) {
                    preferences.setVisible(true);
                }
            }
        });
        final JMenuItem previewItem = new JMenuItem("View Dither Preview");
        previewItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (!preview.isShowing()) {
                    preview.setVisible(true);
                    processPreview();
                }
            }
        });
        optionsMenu.add(previewItem);
        optionsMenu.addSeparator();
        optionsMenu.add(preferencesItem);
        menubar.add(optionsMenu);
        JMenu about = new JMenu("About");
        JMenuItem aboutZx = new JMenuItem("About Image to ZX Spec");
        about.add(aboutZx);
        aboutZx.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                long maxHeap = Runtime.getRuntime().maxMemory();
                long currentHeapUse = Runtime.getRuntime().totalMemory();
                int cpus = Runtime.getRuntime().availableProcessors();
                JOptionPane.showMessageDialog(null, "Image to ZX Spec is a simple to use program which applies a Sinclair ZX Spectrum\n" + "effect to images, creates actual Spectrum compatible slideshows from images or\n" + "Spectrum \"video\" from compatible video files (see JMF 2.1.1 Cross Platform formats).\n\n" + "The software is fully cross platform and multi CPU capable and uses aggressive\n" + "memory options to enable some of the fastest conversion rates possible.\n\n" + "PNG is the default output of choice since it doesn't introduce unwanted artifacts,\n" + "and for most common usages of this program results in smaller files than JPEG.\n" + "SCR (Screen) output is also available, however any image is resized to 256x192\n" + "pixels regardless of chosen options.\n" + "TAP (Tape) format will convert images into a slideshow or video (type determined at\n" + "runtime) for use on emulators or a real Spectrum (with suitable conversion of .tap\n" + "to real tape).\n\n" + "The Image to ZX Spec code does not use more than the basic Java 1.6 API and Java\n" + "Media Framework (JMF) as I wanted to teach myself about low level colour dithering,\n" + "image manipulation and new Java threading capabilities.\n\n" + "Image To ZX Spec is licensed under the GNU General Public Licence (GPL) 2.0 - no \n" + "warranty is provided. You are free to make deriative works, as long as you release\n" + "the amended source code - full details can be found in the license text that\n" + "accompanied this software.\n" + "The software has been driven forward on features and rapid prototyping to prevent the\n" + "project stalling so I'm making no apologies for any sloppy code you may still find :)\n\n" + "This software is copyright Silent Software 2010 (Benjamin Brown).\n\n" + "Processors: " + cpus + "\n" + "Used Java Memory: " + currentHeapUse / 1024 / 1024 + "MB (same as total if aggressive settings)\n" + "Total Java Memory: " + maxHeap / 1024 / 1024 + "MB\n\n" + "Visit Silent Software at http://www.silentsoftware.co.uk", "About Image to ZX Spec", JOptionPane.INFORMATION_MESSAGE, IMAGE_ICON);
            }
        });
        menubar.add(about);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menubar);
        frame.setVisible(true);
        frame.setResizable(false);
        try {
            specLogo = ImageIO.read(ImageToZxSpec.class.getResource("/sinclair.png").openStream());
            Dimension dim = new Dimension(480, 240 + bottomPane.getHeight() + frame.getInsets().top + frame.getInsets().bottom + menubar.getHeight());
            frame.setSize(dim);
            frame.setPreferredSize(dim);
            frame.repaint();
            frame.setLocationRelativeTo(null);
        } catch (IOException e1) {
        }
    }
