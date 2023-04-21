        public void run() {
            try {
                File file = new File(filename);
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                AudioFormat af = ais.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
                if (!AudioSystem.isLineSupported(info)) {
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
                ais.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
