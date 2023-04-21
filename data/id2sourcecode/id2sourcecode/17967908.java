    public static void main(String[] args) {
        if (args.length == 0) System.exit(1);
        AudioInputStream rawInputStream;
        SourceDataLine playLine;
        try {
            rawInputStream = AudioSystem.getAudioInputStream(new File(args[0]));
            playLine = AudioSystem.getSourceDataLine(null);
            playLine.open(rawInputStream.getFormat());
            playLine.start();
            int len;
            byte[] buffer = new byte[playLine.getBufferSize()];
            while ((len = rawInputStream.read(buffer)) >= 0) playLine.write(buffer, 0, len);
            playLine.drain();
            playLine.stop();
            playLine.close();
            rawInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
