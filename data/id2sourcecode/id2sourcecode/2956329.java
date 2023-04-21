    public ChatroomPane(Chatroom par) {
        super(JSplitPane.VERTICAL_SPLIT);
        parent = par;
        sayHello = new String("\t------====  Welcome to the Chat Room  ====------\n" + "\t  ------====     What do U wanna say ?   ====------\n");
        tp_historymsg = new JTextPane();
        historymsg_save = new String();
        historymsg_save += sayHello;
        styledDoc = tp_historymsg.getStyledDocument();
        normal = styledDoc.addStyle("normal", null);
        StyleConstants.setFontFamily(normal, "SansSerif");
        blue = styledDoc.addStyle("blue", normal);
        StyleConstants.setForeground(blue, Color.blue);
        green = styledDoc.addStyle("green", normal);
        StyleConstants.setForeground(green, Color.GREEN.darker());
        gray = styledDoc.addStyle("gray", normal);
        StyleConstants.setForeground(gray, Color.GRAY);
        red = styledDoc.addStyle("red", normal);
        StyleConstants.setForeground(red, Color.red);
        bold = styledDoc.addStyle("bold", normal);
        StyleConstants.setBold(bold, true);
        italic = styledDoc.addStyle("italic", normal);
        StyleConstants.setItalic(italic, true);
        bigSize = styledDoc.addStyle("bigSize", normal);
        StyleConstants.setFontSize(bigSize, 24);
        styledDoc.setLogicalStyle(0, red);
        tp_historymsg.replaceSelection(sayHello);
        tp_historymsg.setBackground(new Color(180, 250, 250));
        tp_historymsg.setSelectionColor(Color.YELLOW);
        tp_historymsg.setEditable(false);
        sp_historymsg = new JScrollPane(tp_historymsg);
        sp_historymsg.setAutoscrolls(true);
        p_inputpaneAndButtons = new JPanel();
        tp_input = new JTextPane();
        tp_input.setToolTipText(getHtmlText("Input your message and press \"Send\" <br>or press Ctrl+Enter"));
        tp_input.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();
                if (keyCode == KeyEvent.VK_ENTER && event.isControlDown()) {
                    System.out.println("You press the combo-key : Ctrl+Enter");
                    sendMessage();
                }
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        sp_input = new JScrollPane(tp_input);
        p_buttons = new JPanel();
        Dimension buttonSize = new Dimension(26, 26);
        b_emoticon = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "emoticon.png"));
        b_emoticon.setToolTipText(getHtmlText("Insert a emoticon"));
        b_emoticon.setActionCommand("Emoticon");
        b_emoticon.addActionListener(this);
        b_emoticon.setSize(buttonSize);
        b_emoticon.setPreferredSize(buttonSize);
        b_emoticon.setMaximumSize(buttonSize);
        b_emoticon.setMinimumSize(buttonSize);
        selFace = new FaceDialog("Insert a face", true, SystemPath.FACES_RESOURCE_PATH);
        selFace.setBounds(450, 350, FaceDialog.FACECELLWIDTH * FaceDialog.FACECOLUMNS, FaceDialog.FACECELLHEIGHT * FaceDialog.FACEROWS + 30);
        selFace.pack();
        b_nudge = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "nudge.png"));
        b_nudge.setToolTipText(getHtmlText("Rock and Roll !"));
        b_nudge.setActionCommand("Nudge");
        b_nudge.addActionListener(this);
        b_nudge.setSize(buttonSize);
        b_nudge.setPreferredSize(buttonSize);
        b_nudge.setMaximumSize(buttonSize);
        b_nudge.setMinimumSize(buttonSize);
        b_sendPic = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "sendpic.png"));
        b_sendPic.setToolTipText(getHtmlText("Send a picture"));
        b_sendPic.setActionCommand("SendPic");
        b_sendPic.addActionListener(this);
        b_sendPic.setSize(buttonSize);
        b_sendPic.setPreferredSize(buttonSize);
        b_sendPic.setMaximumSize(buttonSize);
        b_sendPic.setMinimumSize(buttonSize);
        b_sendFile = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "sendfile.png"));
        b_sendFile.setToolTipText(getHtmlText("Send a file"));
        b_sendFile.setActionCommand("SendFile");
        b_sendFile.addActionListener(this);
        b_sendFile.setSize(buttonSize);
        b_sendFile.setPreferredSize(buttonSize);
        b_sendFile.setMaximumSize(buttonSize);
        b_sendFile.setMinimumSize(buttonSize);
        b_snapshot = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "snapshot.png"));
        b_snapshot.setToolTipText(getHtmlText("Snap it !"));
        b_snapshot.setActionCommand("Snapshot");
        b_snapshot.addActionListener(this);
        b_snapshot.setSize(buttonSize);
        b_snapshot.setPreferredSize(buttonSize);
        b_snapshot.setMaximumSize(buttonSize);
        b_snapshot.setMinimumSize(buttonSize);
        b_snapconfig = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "snapconfig.png"));
        b_snapconfig.setMargin(new Insets(0, 0, 0, 0));
        b_snapconfig.setToolTipText(getHtmlText("Snap Config"));
        b_snapconfig.setActionCommand("SnapshotConfig");
        b_snapconfig.addActionListener(this);
        b_snapconfig.setSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        b_snapconfig.setPreferredSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        b_snapconfig.setMaximumSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        b_snapconfig.setMinimumSize(new Dimension(buttonSize.width / 2, buttonSize.height));
        menuSnap = new JPopupMenu();
        doSnap = new JMenuItem("Let's GO!");
        hideFrame = new JCheckBoxMenuItem("Hide this window while snapping", true);
        doSnap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (hideFrame.getState()) {
                    parent.setState(JFrame.ICONIFIED);
                }
                try {
                    Thread.sleep(300);
                    Robot ro = new Robot();
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Dimension screenSize = tk.getScreenSize();
                    Rectangle rec = new Rectangle(0, 0, screenSize.width, screenSize.height);
                    BufferedImage buffImg = ro.createScreenCapture(rec);
                    final JDialog fakeWin = new JDialog(parent, true);
                    fakeWin.addKeyListener(new KeyListener() {

                        public void keyPressed(KeyEvent event) {
                            int keyCode = event.getKeyCode();
                            if (keyCode == KeyEvent.VK_ESCAPE) {
                                fakeWin.dispose();
                            }
                        }

                        public void keyTyped(KeyEvent e) {
                        }

                        public void keyReleased(KeyEvent e) {
                        }
                    });
                    ScreenCapturer temp = new ScreenCapturer(fakeWin, buffImg, screenSize.width, screenSize.height);
                    fakeWin.getContentPane().add(temp, BorderLayout.CENTER);
                    fakeWin.setUndecorated(true);
                    fakeWin.setSize(screenSize);
                    fakeWin.setVisible(true);
                    fakeWin.setAlwaysOnTop(true);
                    parent.setState(JFrame.NORMAL);
                    buffImg = temp.getWhatWeGot();
                    if (buffImg != null) {
                        ChatroomPane.this.sendAPicture(buffImg);
                    } else {
                        System.out.println("phew~we got nothing.");
                    }
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
        });
        menuSnap.add(doSnap);
        menuSnap.addSeparator();
        menuSnap.add(hideFrame);
        menuSnap.pack();
        tb_encrypt = new JToggleButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "unlock.png"));
        tb_encrypt.setToolTipText(getHtmlText("Encrypt or not"));
        tb_encrypt.setSelectedIcon(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "lock.png"));
        tb_encrypt.setSelected(true);
        tb_encrypt.setSize(buttonSize);
        tb_encrypt.setPreferredSize(buttonSize);
        tb_encrypt.setMaximumSize(buttonSize);
        tb_encrypt.setMinimumSize(buttonSize);
        b_send = new JButton(new ImageIcon(SystemPath.ICONS_RESOURCE_PATH + "send.png"));
        b_send.setMnemonic('S');
        b_send.setActionCommand("Send");
        b_send.setToolTipText(getHtmlText("Send"));
        b_send.addActionListener(this);
        b_send.setSize(buttonSize);
        b_send.setPreferredSize(buttonSize);
        b_send.setMaximumSize(buttonSize);
        b_send.setMinimumSize(buttonSize);
        p_buttons.setOpaque(false);
        p_buttons.setLayout(new BoxLayout(p_buttons, BoxLayout.X_AXIS));
        p_buttons.add(b_emoticon);
        p_buttons.add(b_nudge);
        p_buttons.add(b_sendPic);
        p_buttons.add(b_sendFile);
        p_buttons.add(b_snapshot);
        p_buttons.add(b_snapconfig);
        p_buttons.add(Box.createHorizontalGlue());
        p_buttons.add(tb_encrypt);
        p_buttons.add(b_send);
        p_inputpaneAndButtons.setOpaque(false);
        p_inputpaneAndButtons.setLayout(new BorderLayout());
        p_inputpaneAndButtons.add(p_buttons, BorderLayout.NORTH);
        p_inputpaneAndButtons.add(sp_input, BorderLayout.CENTER);
        this.setSize(new Dimension(PeerChatroom.WIDTH_DEFLT, PeerChatroom.HEIGHT_DEFLT - 35));
        this.setDividerLocation(0.65);
        this.setResizeWeight(0.62d);
        this.setDividerSize(3);
        this.add(sp_historymsg);
        this.add(p_inputpaneAndButtons);
    }
