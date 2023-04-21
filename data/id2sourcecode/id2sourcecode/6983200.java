    public ClientVirtualScreen() throws Exception {
        instance = this;
        ClientStartScreen.instance.tFieldScreenZoom = new JLabel();
        ClientStartScreen.instance.tFieldScreenZoom.setBounds(30, 120, 200, 20);
        ClientStartScreen.instance.tFieldScreenZoom.setText(ClientStartScreen.instance.label734);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.tFieldScreenZoom);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        ClientVirtualScreenBean.screenratio = screenSize.getHeight() / screenSize.getWidth();
        ClientVirtualScreenBean.screenWidthMax = Double.valueOf(screenSize.getWidth()).intValue();
        ClientVirtualScreenBean.screenHeightMax = Double.valueOf(screenSize.getHeight()).intValue();
        ClientVirtualScreenBean.vScreenHeight = Long.valueOf(Math.round(ClientVirtualScreenBean.vScreenWidth * ClientVirtualScreenBean.screenratio)).intValue();
        int width = ClientVirtualScreenBean.vScreenWidth;
        int height = Long.valueOf(Math.round(width * ClientVirtualScreenBean.screenratio)).intValue();
        ClientStartScreen.instance.vScreenIconLeft = new JLabel();
        ClientStartScreen.instance.vScreenIconLeft.setBounds(14, 162 + (height / 2), 32, 16);
        ClientStartScreen.instance.vScreenIconRight = new JLabel();
        ClientStartScreen.instance.vScreenIconRight.setBounds(30 + width - 16, 162 + (height / 2), 32, 16);
        ClientStartScreen.instance.vScreenIconUp = new JLabel();
        ClientStartScreen.instance.vScreenIconUp.setBounds(30 + (width / 2) - 8, 162 - 8, 16, 32);
        ClientStartScreen.instance.vScreenIconDown = new JLabel();
        ClientStartScreen.instance.vScreenIconDown.setBounds(30 + (width / 2) - 8, 162 + height - 8, 16, 32);
        Image im_left = ImageIO.read(ClientStartScreen.class.getResource("/1leftarrow.png"));
        ImageIcon iIcon1 = new ImageIcon(im_left);
        Image im_right = ImageIO.read(ClientStartScreen.class.getResource("/1rightarrow.png"));
        ImageIcon iIcon2 = new ImageIcon(im_right);
        Image im_up = ImageIO.read(ClientStartScreen.class.getResource("/1uparrow.png"));
        ImageIcon iIcon3 = new ImageIcon(im_up);
        Image im_down = ImageIO.read(ClientStartScreen.class.getResource("/1downarrow.png"));
        ImageIcon iIcon4 = new ImageIcon(im_down);
        JLabel jLab1 = new JLabel(iIcon1);
        jLab1.setBounds(0, 0, 16, 16);
        JLabel jLab2 = new JLabel(iIcon2);
        jLab2.setBounds(16, 0, 16, 16);
        ClientStartScreen.instance.vScreenIconLeft.add(jLab1);
        ClientStartScreen.instance.vScreenIconLeft.add(jLab2);
        ClientStartScreen.instance.vScreenIconLeft.setToolTipText(ClientStartScreen.instance.label735);
        ClientVirtualScreenXMouseListener xLeftMouseListener = new ClientVirtualScreenXMouseListener();
        ClientStartScreen.instance.vScreenIconLeft.addMouseListener(xLeftMouseListener);
        ClientStartScreen.instance.vScreenIconLeft.addMouseMotionListener(xLeftMouseListener);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vScreenIconLeft);
        JLabel jLab3 = new JLabel(iIcon1);
        jLab3.setBounds(0, 0, 16, 16);
        JLabel jLab4 = new JLabel(iIcon2);
        jLab4.setBounds(16, 0, 16, 16);
        ClientStartScreen.instance.vScreenIconRight.add(jLab3);
        ClientStartScreen.instance.vScreenIconRight.add(jLab4);
        ClientStartScreen.instance.vScreenIconRight.setToolTipText(ClientStartScreen.instance.label735);
        ClientVirtualScreenWidthMouseListener widthMouseListener = new ClientVirtualScreenWidthMouseListener();
        ClientStartScreen.instance.vScreenIconRight.addMouseListener(widthMouseListener);
        ClientStartScreen.instance.vScreenIconRight.addMouseMotionListener(widthMouseListener);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vScreenIconRight);
        JLabel jLab5 = new JLabel(iIcon3);
        jLab5.setBounds(0, 0, 16, 16);
        JLabel jLab6 = new JLabel(iIcon4);
        jLab6.setBounds(0, 16, 16, 16);
        ClientStartScreen.instance.vScreenIconUp.add(jLab5);
        ClientStartScreen.instance.vScreenIconUp.add(jLab6);
        ClientStartScreen.instance.vScreenIconUp.setToolTipText(ClientStartScreen.instance.label737);
        ClientVirtualScreenYMouseListener yMouseListener = new ClientVirtualScreenYMouseListener();
        ClientStartScreen.instance.vScreenIconUp.addMouseListener(yMouseListener);
        ClientStartScreen.instance.vScreenIconUp.addMouseMotionListener(yMouseListener);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vScreenIconUp);
        JLabel jLab7 = new JLabel(iIcon3);
        jLab7.setBounds(0, 0, 16, 16);
        JLabel jLab8 = new JLabel(iIcon4);
        jLab8.setBounds(0, 16, 16, 16);
        ClientStartScreen.instance.vScreenIconDown.add(jLab7);
        ClientStartScreen.instance.vScreenIconDown.add(jLab8);
        ClientStartScreen.instance.vScreenIconDown.setToolTipText(ClientStartScreen.instance.label737);
        ClientVirtualScreenHeightMouseListener heightMouseListener = new ClientVirtualScreenHeightMouseListener();
        ClientStartScreen.instance.vScreenIconDown.addMouseListener(heightMouseListener);
        ClientStartScreen.instance.vScreenIconDown.addMouseMotionListener(heightMouseListener);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vScreenIconDown);
        ClientStartScreen.instance.virtualScreen = new ClientBlankArea(new Color(255, 255, 255, 100));
        ClientStartScreen.instance.virtualScreen.setOpaque(true);
        ClientStartScreen.instance.virtualScreen.setHorizontalAlignment(SwingConstants.LEFT);
        ClientStartScreen.instance.virtualScreen.setVerticalAlignment(SwingConstants.TOP);
        ClientStartScreen.instance.virtualScreen.setText(ClientVirtualScreenBean.screenWidthMax + ":" + ClientVirtualScreenBean.screenHeightMax);
        ClientStartScreen.instance.virtualScreen.setBounds(30, 170, ClientVirtualScreenBean.vScreenWidth, ClientVirtualScreenBean.vScreenHeight);
        ClientVirtualScreenMouseListener vListener = new ClientVirtualScreenMouseListener();
        ClientStartScreen.instance.virtualScreen.addMouseListener(vListener);
        ClientStartScreen.instance.virtualScreen.addMouseMotionListener(vListener);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.virtualScreen);
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage imageScreen = robot.createScreenCapture(screenRectangle);
        Image img = imageScreen.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        log.debug("img" + img);
        ImageIcon image = new ImageIcon(img);
        ClientStartScreen.instance.blankArea = new JLabel(image);
        ClientStartScreen.instance.blankArea.setBounds(30, 170, width, height);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.blankArea);
        ClientVirtualScreenBean.vScreenSpinnerX = 0;
        ClientStartScreen.instance.vscreenXLabel = new JLabel();
        ClientStartScreen.instance.vscreenXLabel.setText(ClientStartScreen.instance.label738);
        ClientStartScreen.instance.vscreenXLabel.setBounds(250, 170, 150, 24);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vscreenXLabel);
        ClientStartScreen.instance.jVScreenXSpin = new JSpinner(new SpinnerNumberModel(ClientVirtualScreenBean.vScreenSpinnerX, 0, ClientVirtualScreenBean.screenWidthMax, 1));
        ClientStartScreen.instance.jVScreenXSpin.setBounds(400, 170, 60, 24);
        ClientStartScreen.instance.jVScreenXSpin.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                calcNewValueXSpin();
            }
        });
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.jVScreenXSpin);
        ClientVirtualScreenBean.vScreenSpinnerY = 0;
        ClientStartScreen.instance.vscreenYLabel = new JLabel();
        ClientStartScreen.instance.vscreenYLabel.setText(ClientStartScreen.instance.label739);
        ClientStartScreen.instance.vscreenYLabel.setBounds(250, 200, 150, 24);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vscreenYLabel);
        ClientStartScreen.instance.jVScreenYSpin = new JSpinner(new SpinnerNumberModel(ClientVirtualScreenBean.vScreenSpinnerY, 0, ClientVirtualScreenBean.screenHeightMax, 1));
        ClientStartScreen.instance.jVScreenYSpin.setBounds(400, 200, 60, 24);
        ClientStartScreen.instance.jVScreenYSpin.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                calcNewValueYSpin();
            }
        });
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.jVScreenYSpin);
        ClientVirtualScreenBean.vScreenSpinnerWidth = ClientVirtualScreenBean.screenWidthMax;
        ClientStartScreen.instance.vscreenWidthLabel = new JLabel();
        ClientStartScreen.instance.vscreenWidthLabel.setText(ClientStartScreen.instance.label740);
        ClientStartScreen.instance.vscreenWidthLabel.setBounds(250, 240, 150, 24);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vscreenWidthLabel);
        ClientStartScreen.instance.jVScreenWidthSpin = new JSpinner(new SpinnerNumberModel(ClientVirtualScreenBean.vScreenSpinnerWidth, 0, ClientVirtualScreenBean.screenWidthMax, 1));
        ClientStartScreen.instance.jVScreenWidthSpin.setBounds(400, 240, 60, 24);
        ClientStartScreen.instance.jVScreenWidthSpin.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                calcNewValueWidthSpin();
            }
        });
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.jVScreenWidthSpin);
        ClientVirtualScreenBean.vScreenSpinnerHeight = ClientVirtualScreenBean.screenHeightMax;
        ClientStartScreen.instance.vscreenHeightLabel = new JLabel();
        ClientStartScreen.instance.vscreenHeightLabel.setText(ClientStartScreen.instance.label741);
        ClientStartScreen.instance.vscreenHeightLabel.setBounds(250, 270, 150, 24);
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.vscreenHeightLabel);
        ClientStartScreen.instance.jVScreenHeightSpin = new JSpinner(new SpinnerNumberModel(ClientVirtualScreenBean.vScreenSpinnerHeight, 0, ClientVirtualScreenBean.screenHeightMax, 1));
        ClientStartScreen.instance.jVScreenHeightSpin.setBounds(400, 270, 60, 24);
        ClientStartScreen.instance.jVScreenHeightSpin.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                calcNewValueHeightSpin();
            }
        });
        ClientStartScreen.instance.t.add(ClientStartScreen.instance.jVScreenHeightSpin);
    }
