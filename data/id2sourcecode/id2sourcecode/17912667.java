    public void sonido(String nombre) {
        try {
            String archivo;
            archivo = new String(getClass().getResource("/recursos/audio/" + nombre + ".wav").getFile());
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(archivo));
            AudioFormat af = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("unsupported line");
                System.exit(0);
            }
            int frameRate = (int) af.getFrameRate();
            int frameSize = af.getFrameSize();
            int bufSize = frameRate * frameSize / 10;
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(af, bufSize);
            line.start();
            byte[] data = new byte[bufSize];
            int bytesRead;
            while ((bytesRead = ais.read(data, 0, data.length)) != -1) line.write(data, 0, bytesRead);
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e2) {
            System.out.println("Error Audio");
        }
    }
