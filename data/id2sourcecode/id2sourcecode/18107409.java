    public void sendScreenshot(final ChatRoomButton button, final ChatRoom room) {
        button.setEnabled(false);
        final MainWindow mainWindow = SparkManager.getMainWindow();
        final ChatFrame chatFrame = SparkManager.getChatManager().getChatContainer().getChatFrame();
        final boolean mainWindowVisible = mainWindow.isVisible();
        final boolean chatFrameVisible = chatFrame.isVisible();
        if (mainWindowVisible) {
            mainWindow.setVisible(false);
        }
        if (chatFrameVisible) {
            chatFrame.setVisible(false);
        }
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                try {
                    Thread.sleep(1000);
                    Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    return robot.createScreenCapture(area);
                } catch (Throwable e) {
                    Log.error(e);
                    if (mainWindowVisible) {
                        mainWindow.setVisible(true);
                    }
                    if (chatFrameVisible) {
                        chatFrame.setVisible(true);
                    }
                }
                return null;
            }

            public void finished() {
                bufferedImage = (BufferedImage) get();
                if (bufferedImage == null) {
                    JOptionPane.showMessageDialog(null, Res.getString("title.error"), "Unable to process screenshot.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                final Frame frame = new Frame();
                frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                selectionPanel.setImage(bufferedImage);
                selectionPanel.validate();
                selectionPanel.addMouseListener(new MouseAdapter() {

                    public void mouseReleased(MouseEvent e) {
                        Rectangle clip = selectionPanel.getClip();
                        BufferedImage newImage = null;
                        try {
                            newImage = bufferedImage.getSubimage((int) clip.getX(), (int) clip.getY(), (int) clip.getWidth(), (int) clip.getHeight());
                        } catch (Exception e1) {
                        }
                        if (newImage != null) {
                            sendImage(newImage, room);
                            bufferedImage = null;
                            selectionPanel.clear();
                        }
                        frame.dispose();
                        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        if (mainWindowVisible) {
                            mainWindow.setVisible(true);
                        }
                        if (chatFrameVisible) {
                            chatFrame.setVisible(true);
                        }
                        selectionPanel.removeMouseListener(this);
                    }
                });
                frame.addKeyListener(new KeyAdapter() {

                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                            frame.dispose();
                            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            if (mainWindowVisible) {
                                mainWindow.setVisible(true);
                            }
                            if (chatFrameVisible) {
                                chatFrame.setVisible(true);
                            }
                        }
                    }
                });
                frame.setSize(bufferedImage.getWidth(null), bufferedImage.getHeight());
                frame.add(selectionPanel);
                frame.setUndecorated(true);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gs = ge.getDefaultScreenDevice();
                if (gs.isFullScreenSupported()) {
                    gs.setFullScreenWindow(frame);
                } else {
                    frame.setVisible(true);
                }
                button.setEnabled(true);
            }
        };
        worker.start();
    }
