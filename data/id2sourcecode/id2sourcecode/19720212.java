    private static void playStream(final File fileToPlay) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        final AudioInputStream streamToPlay = AudioSystem.getAudioInputStream(fileToPlay);
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
            line.drain();
            streamToPlay.close();
            line.stop();
            line.close();
        }
    }
