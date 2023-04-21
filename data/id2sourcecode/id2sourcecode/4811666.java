        public void run() {
            final int blockWidth = 32;
            final int blockHeight = 32;
            final int timeBetweenFrames = 1000;
            int frameCounter = 0;
            try {
                Robot robot = new Robot();
                this.previousItems = null;
                while (active) {
                    final long ctime = System.currentTimeMillis();
                    BufferedImage image_raw = robot.createScreenCapture(new Rectangle(x, y, width, height));
                    int scaledWidth = width;
                    int scaledHeight = height;
                    byte[] current = null;
                    if (scaleFactor != 1F) {
                        logger.debug("Calc new Scaled Instance ", scaleFactor);
                        scaledWidth = Float.valueOf(Math.round(width * scaleFactor)).intValue();
                        scaledHeight = Float.valueOf(Math.round(height * scaleFactor)).intValue();
                        Image img = image_raw.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                        BufferedImage image_scaled = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);
                        Graphics2D biContext = image_scaled.createGraphics();
                        biContext.drawImage(img, 0, 0, null);
                        current = toBGR(image_scaled);
                    } else {
                        current = toBGR(image_raw);
                    }
                    try {
                        timestamp += (1000000 / timeBetweenFrames);
                        final byte[] screenBytes = encode(current, this.previousItems, blockWidth, blockHeight, scaledWidth, scaledHeight);
                        pushVideo(screenBytes.length, screenBytes, timestamp);
                        this.previousItems = current;
                        if (++frameCounter % 100 == 0) this.previousItems = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final int spent = (int) (System.currentTimeMillis() - ctime);
                    sendCursorStatus();
                    Thread.sleep(Math.max(0, timeBetweenFrames - spent));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
