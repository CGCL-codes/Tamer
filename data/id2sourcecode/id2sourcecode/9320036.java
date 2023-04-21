    public void startPlayCompressedSound(String filepath, boolean loop) {
        stop();
        AudioInputStream din = null;
        looping = loop;
        do {
            try {
                File file = new File(filepath);
                AudioInputStream in = AudioSystem.getAudioInputStream(file);
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                currentLine = (SourceDataLine) AudioSystem.getLine(info);
                if (currentLine != null) {
                    currentLine.addLineListener(this);
                    currentLine.open(decodedFormat);
                    setVolume(volume);
                    setMute(mute);
                    byte[] data = new byte[decodedFormat.getSampleSizeInBits()];
                    currentLine.start();
                    int nBytesRead = 0;
                    while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                        currentLine.write(data, 0, nBytesRead);
                    }
                    currentLine.drain();
                    currentLine.stop();
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                logger.log(Level.SEVERE, "Issues when playing compressed sound", e);
            } finally {
                if (din != null) {
                    try {
                        din.close();
                        din = null;
                    } catch (IOException e) {
                    }
                }
            }
        } while (looping);
    }
