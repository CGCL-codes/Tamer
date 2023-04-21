    @Override
    protected void frameInit() {
        super.frameInit();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));
        try {
            robot = new Robot();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
        imagePanel = new JPanel() {

            private static final long serialVersionUID = 7793240458833917848L;

            public void paintComponent(final Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
                g.drawRoundRect(halfWidth - halfRoundSize, halfHeight - halfRoundSize, roundSize, roundSize, arcSize, arcSize);
                g.drawRect(halfWidth - halfZoom, halfWidth - halfZoom, zoom, zoom);
            }
        };
        add(imagePanel);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                char c = e.getKeyChar();
                switch(c) {
                    case '+':
                        if (zoom < MAX_ZOOM) {
                            zoom *= 2;
                        }
                        break;
                    case '-':
                        if (zoom > MIN_ZOOM) {
                            zoom /= 2;
                        }
                        break;
                    default:
                        if (t.isRunning()) {
                            t.stop();
                            Point p = MouseInfo.getPointerInfo().getLocation();
                            if (studio != null) {
                                studio.setMainColor(robot.getPixelColor(p.x, p.y));
                            }
                            dispose();
                        } else {
                            t.start();
                        }
                        break;
                }
                halfZoom = (zoom >> 1);
                roundSize = 10 * (zoom / MIN_ZOOM);
                arcSize = roundSize >> 1;
                halfRoundSize = (arcSize);
            }
        });
        t = new Timer(100, new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                Point currentMousePos = MouseInfo.getPointerInfo().getLocation();
                image = robot.createScreenCapture(new Rectangle(currentMousePos.x - width / zoom / 2, currentMousePos.y - height / zoom / 2, width / zoom, height / zoom)).getScaledInstance(width, height, Image.SCALE_DEFAULT);
                repaint();
            }
        });
        t.start();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(final WindowEvent e) {
                if (studio != null) {
                    studio.setState(JFrame.NORMAL);
                }
            }
        });
        pack();
        setVisible(true);
    }
