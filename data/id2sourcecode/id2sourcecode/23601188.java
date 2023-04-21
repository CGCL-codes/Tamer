            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setApproveButtonText(Messages.getString("MindRaiderJFrame.screenshot"));
                fc.setControlButtonsAreShown(true);
                fc.setDialogTitle(Messages.getString("MindRaiderJFrame.chooseScreenshotDirectory"));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                String exportDirectory = MindRaider.profile.getHomeDirectory() + File.separator + "Screenshots";
                Utils.createDirectory(exportDirectory);
                fc.setCurrentDirectory(new File(exportDirectory));
                int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final String filename = fc.getSelectedFile().getAbsolutePath() + File.separator + "screenshot.jpg";
                    new Thread() {

                        public void run() {
                            OutputStream file = null;
                            try {
                                file = new FileOutputStream(filename);
                                Robot robot = new Robot();
                                robot.delay(1000);
                                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(file);
                                encoder.encode(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
                            } catch (Exception e1) {
                                logger.error("Unable to capture screen!", e1);
                            } finally {
                                if (file != null) {
                                    try {
                                        file.close();
                                    } catch (IOException e1) {
                                        logger.error("Unable to close stream", e1);
                                    }
                                }
                            }
                        }
                    }.start();
                }
            }
