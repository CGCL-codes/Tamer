    private void playSong(String fileName) {
        paused = false;
        AudioInputStream din = null;
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(fileName));
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            if (line != null) {
                line.open(decodedFormat);
                byte[] data = new byte[4096];
                line.start();
                int nBytesRead;
                synchronized (lock) {
                    while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                        if (stop) break;
                        while (paused) {
                            if (line.isRunning()) {
                                line.stop();
                            }
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                        if (!line.isRunning()) {
                            line.start();
                        }
                        line.write(data, 0, nBytesRead);
                    }
                }
                line.drain();
                line.stop();
                line.close();
                din.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (din != null) {
                try {
                    din.close();
                } catch (IOException e) {
                }
            }
        }
    }
