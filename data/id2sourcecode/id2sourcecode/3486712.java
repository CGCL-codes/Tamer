    private static void playStream(final AudioInputStream streamToPlay) throws LineUnavailableException, IOException {
        if (1 == 1) {
            streamToPlay.close();
            System.gc();
            return;
        }
        final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, streamToPlay.getFormat()));
        line.open();
        line.start();
        try {
            byte[] buf = new byte[1024];
            int readCnt;
            while ((readCnt = streamToPlay.read(buf, 0, buf.length)) != -1) {
                line.write(buf, 0, readCnt);
            }
        } finally {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            streamToPlay.close();
            line.stop();
            line.close();
        }
    }
