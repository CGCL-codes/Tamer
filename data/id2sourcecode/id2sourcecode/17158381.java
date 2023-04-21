    public void run() {
        running = true;
        byte[] previous = null;
        while (running) {
            final long ctime = System.currentTimeMillis();
            BufferedImage image = robot.createScreenCapture(new Rectangle(x, y, width, height));
            byte[] current = toBGR(image);
            try {
                final byte[] encoded = videoEncoder.encode(current, previous, width, height);
                if (previous == null) {
                    consumer.putData(DataType.KEY_FRAME, System.currentTimeMillis(), encoded, encoded.length);
                } else {
                    consumer.putData(DataType.INTER_FRAME, System.currentTimeMillis(), encoded, encoded.length);
                }
                previous = current;
                if (++frameCounter % 10 == 0) previous = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            final int spent = (int) (System.currentTimeMillis() - ctime);
            try {
                Thread.sleep(Math.max(0, timeBetweenFrames - spent));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
