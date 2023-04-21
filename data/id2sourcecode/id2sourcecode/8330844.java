    protected void constructFrame() {
        frame = this;
        try {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(fileFilter);
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(actionOpen);
        menu.add(actionSave);
        menu.addSeparator();
        menu.add(actionPause);
        menu.add(actionStep);
        menu.add(actionPerformanceTest);
        menu.addSeparator();
        menu.add(actionExit);
        menuBar.add(menu);
        menu = new JMenu("Edit");
        menu.add(actionCut);
        menu.add(actionCopy);
        menu.add(actionPaste);
        menu.addSeparator();
        menu.add(actionFill);
        menuBar.add(menu);
        menu = new JMenu("View");
        menu.add(statsToggleCbmi = new JCheckBoxMenuItem(actionToggleStatsPanel));
        menu.add(fullScreenCbmi = new JCheckBoxMenuItem(actionFullScreen));
        menu.add(actionEscapeFullScreen);
        menuBar.add(menu);
        GraphicsDevice gv = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        fullScreenCbmi.setEnabled(gv.isFullScreenSupported());
        menu = new JMenu("Brush");
        for (BrushSelectionAction bb : basicBrushes) menu.add(bb);
        menu.addSeparator();
        JMenu mBrushes = new JMenu("More Brushes");
        for (BrushSelectionAction ab : moreBrushes) mBrushes.add(ab);
        menu.add(mBrushes);
        mBrushes = new JMenu("Fun Brushes");
        for (BrushSelectionAction fb : funBrushes) mBrushes.add(fb);
        menu.add(mBrushes);
        menu.addSeparator();
        menu.add(actionRotateLeft);
        menu.add(actionRotateRight);
        menu.addSeparator();
        menu.add(actionGrowBrush);
        menu.add(actionShrinkBrush);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        menu = new JMenu("Elements");
        for (ElementSelectionAction ea : elementActions) menu.add(ea);
        menuBar.add(menu);
        JToolBar toolBar = new JToolBar("Elements");
        for (ElementSelectionAction ea : elementActions) toolBar.add(ea);
        setBackground(backGround);
        worldPanel = new JPanel() {

            public void paint(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics;
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(frameBuffer, 0, 0, null);
            }
        };
        worldPanel.setPreferredSize(new Dimension(WORLD_WIDTH, WORLD_HEIGHT));
        worldPanel.setLayout(null);
        add(worldPanel);
        addComponentListener(new ComponentAdapter() {

            Thread resizeThread = null;

            public void componentResized(ComponentEvent e) {
                (resizeThread = new Thread() {

                    public void run() {
                        try {
                            sleep(RESIZE_DELAY);
                            if (resizeThread == this) resize();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        MouseInputAdapter mia = new MouseInputAdapter() {

            public void mouseClicked(MouseEvent e) {
                paint(e);
            }

            public void mouseDragged(MouseEvent e) {
                paint(e);
            }

            public void paint(MouseEvent e) {
                Element source = null;
                if (e.isShiftDown() && (source = Element.lookup(brushColor).lookupSourceOrOutput()) != null) worldGr.setColor(source.getColor()); else worldGr.setColor(brushColor);
                paintBrushShape(brushShape, worldGr, e.getX(), e.getY());
                forcePaint = true;
            }
        };
        worldPanel.addMouseListener(mia);
        worldPanel.addMouseMotionListener(mia);
        statsPanel = new JPanel() {

            public void paint(Graphics graphics) {
                Graphics2D gr = (Graphics2D) graphics;
                gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gr.setColor(new Color(205, 205, 205));
                gr.fill(gr.getClipBounds());
                gr.setColor(Color.GRAY);
                gr.setFont(Font.decode("Courier"));
                gr.drawString("fps:   " + round(frameRate * 100) / 100.0, 5, 15);
                gr.drawString("sim:   " + round(updatePercent * 100) + "%", 5, 30);
                gr.drawString("paint: " + round(paintPercent * 100) + "%", 5, 45);
                gr.drawString("width:  " + width, 100, 15);
                gr.drawString("height: " + height, 100, 30);
                gr.drawString("pixels: " + (width * height), 100, 45);
                gr.drawString("brush: " + brushName, 195, 15);
                gr.drawString("elmnt: " + brushElement, 195, 30);
            }
        };
        statsPanel.setPreferredSize(new Dimension(300, 55));
        statsPanel.setMinimumSize(new Dimension(150, 55));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
    }
