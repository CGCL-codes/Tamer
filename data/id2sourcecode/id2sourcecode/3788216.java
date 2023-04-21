    private static void createAndShowGUI(AutoKomp ak) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("AutoKomp 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addMouseListener(ak);
        frame.addMouseMotionListener(ak);
        frame.getContentPane().addMouseListener(ak);
        frame.getContentPane().addMouseMotionListener(ak);
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("About");
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        JMenuItem newAction = new JMenuItem("New");
        JMenuItem openAction = new JMenuItem("Open");
        JMenuItem saveAction = new JMenuItem("Save");
        JMenuItem exitAction = new JMenuItem("Exit");
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(exitAction);
        exitAction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        saveAction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("."));
                int returnVal = fc.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    Template template = new Template(image);
                    template.saveTemplate(fc.getSelectedFile().getPath());
                }
            }
        });
        newAction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                frame.hide();
                try {
                    Dimension dim = null;
                    dim = Toolkit.getDefaultToolkit().getScreenSize();
                    Robot robot;
                    robot = new Robot();
                    robot.delay(100);
                    BufferedImage img = robot.createScreenCapture(new Rectangle(1, 1, (int) dim.getWidth(), (int) dim.getHeight()));
                    icon = new ImageIcon(img);
                    label.setIcon(icon);
                    frame.repaint();
                    label.repaint();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                frame.show();
            }
        });
        label = new JLabel("Hello World");
        stopButton = new JButton("Stop");
        stopButton.setVerticalTextPosition(AbstractButton.CENTER);
        stopButton.setHorizontalTextPosition(AbstractButton.LEADING);
        stopButton.setMnemonic(KeyEvent.VK_ESCAPE);
        stopButton.setActionCommand("stop");
        Dimension dim = null;
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        icon = new ImageIcon(ak.getScreen(new Rectangle(1, 1, (int) dim.getWidth(), (int) dim.getHeight())));
        label.setIcon(icon);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }
