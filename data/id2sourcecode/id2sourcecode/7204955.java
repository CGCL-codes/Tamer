    public void run() {
        synchronized (thread) {
            try {
                while (!isDisposed) {
                    thread.wait();
                    if (file != null) {
                        try {
                            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
                            AudioFormat af = ais.getFormat();
                            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
                            if (AudioSystem.isLineSupported(info)) {
                                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                                line.open(af);
                                line.start();
                                byte[] data = new byte[(int) af.getFrameRate() * af.getFrameSize() / 10];
                                int read;
                                while ((read = ais.read(data)) != -1) {
                                    line.write(data, 0, read);
                                }
                                line.drain();
                                line.stop();
                                line.close();
                            }
                        } catch (UnsupportedAudioFileException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (LineUnavailableException e) {
                            e.printStackTrace();
                        } finally {
                            file = null;
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
