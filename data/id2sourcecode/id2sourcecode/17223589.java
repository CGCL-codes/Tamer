        public void run() {
            boolean rescale = false;
            if (width > maxWidth) {
                rescale = true;
            }
            final int timeBetweenFrames = 1000 / frameRate;
            try {
                ScreenCodec screenCodec;
                if ("flashsv1".equals(codec)) screenCodec = new ScreenCodec1(rescale ? maxWidth : width, rescale ? maxHeight : height); else screenCodec = new ScreenCodec2(rescale ? maxWidth : width, rescale ? maxHeight : height);
                while (active) {
                    final long ctime = System.currentTimeMillis();
                    try {
                        BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, width, height));
                        BufferedImage image1 = addCursor(x, y, image);
                        if (rescale) {
                            image1 = scaleImage(image1, maxWidth, maxHeight);
                        }
                        timestamp = System.currentTimeMillis() - startTime;
                        final byte[] screenBytes = screenCodec.encode(image1);
                        pushVideo(screenBytes, timestamp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final int spent = (int) (System.currentTimeMillis() - ctime);
                    final int sleep = Math.max(0, timeBetweenFrames - spent);
                    if (kt < 50) {
                        logger.debug("Sleep " + sleep);
                        System.out.println("Sleep " + sleep);
                    }
                    Thread.sleep(sleep);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
