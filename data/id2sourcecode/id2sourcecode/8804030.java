        protected void setupSound() {
            try {
                audioStream = getAudioStream();
                if (audioStream instanceof AudioInputStream) {
                    audioInputStream = (AudioInputStream) audioStream;
                } else {
                    audioInputStream = AudioSystem.getAudioInputStream(audioStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            audioFormat = audioInputStream.getFormat();
            info = new DataLine.Info(SourceDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(info)) {
                AudioFormat sourceFormat = audioFormat;
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
                audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                audioFormat = audioInputStream.getFormat();
                info = new DataLine.Info(SourceDataLine.class, audioFormat);
            }
            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            buffer = new byte[128000];
            written = 0;
            read = 0;
        }
