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
