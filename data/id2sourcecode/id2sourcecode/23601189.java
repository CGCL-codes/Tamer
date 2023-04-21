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
