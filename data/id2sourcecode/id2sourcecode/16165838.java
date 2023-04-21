        protected void setupSound() {
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F, false);
            info = new DataLine.Info(TargetDataLine.class, audioFormat);
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            audioInputStream = new AudioInputStream(line);
            AudioFormat targetFormat = new AudioFormat(SpeexEncoding.SPEEX_Q6, audioFormat.getSampleRate(), -1, audioFormat.getChannels(), -1, -1, false);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
            audioFormat = audioInputStream.getFormat();
            out = new ByteArrayOutputStream();
            buffer = new byte[128000];
        }
