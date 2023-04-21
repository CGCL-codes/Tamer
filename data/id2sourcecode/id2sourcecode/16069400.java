    public void play(final String pathToSound) {
        Boolean isPlaying = soundsPlaying.get(pathToSound);
        if (isPlaying == null || !isPlaying) {
            soundsPlaying.put(pathToSound, true);
            SourceDataLine auline = null;
            AudioInputStream audioInputStream = null;
            try {
                File soundFile = new File(pathToSound);
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat audioFormat = audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, audioFormat.getSampleSizeInBits());
                boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
                if (!bIsSupportedDirectly) {
                    AudioFormat sourceFormat = audioFormat;
                    AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
                    audioInputStream.close();
                    audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                    audioFormat = audioInputStream.getFormat();
                }
                AudioFormat format = audioInputStream.getFormat();
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
                auline.start();
                int nBytesRead = 0;
                byte[] abData = new byte[524288];
                try {
                    while (nBytesRead != -1) {
                        nBytesRead = audioInputStream.read(abData, 0, abData.length);
                        if (nBytesRead >= 0) {
                            auline.write(abData, 0, nBytesRead);
                        }
                    }
                } finally {
                    try {
                        auline.drain();
                    } catch (Throwable t) {
                    }
                }
            } catch (Throwable t) {
                Raptor.getInstance().onError("Error playing sound " + pathToSound, t);
            } finally {
                try {
                    auline.close();
                } catch (Throwable t) {
                }
                try {
                    audioInputStream.close();
                } catch (Throwable t) {
                }
                soundsPlaying.put(pathToSound, false);
            }
        }
    }
