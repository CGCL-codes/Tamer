        public final void run() {
            if (clip != null && clip.isOpen()) return;
            try {
                openClipStream();
                AudioInputStream ain = AudioSystem.getAudioInputStream(clipStream);
                AudioFormat format = ain.getFormat();
                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                    AudioFormat temp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                    ain = AudioSystem.getAudioInputStream(temp, ain);
                    format = temp;
                }
                DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat(), ((int) ain.getFrameLength() * format.getFrameSize()));
                clip = (Clip) AudioSystem.getLine(info);
                clip.addLineListener(WaveCore.this);
                clip.open(ain);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
                if (volume != 1.0f) {
                    volume = 1.0f;
                    setVolume(volume);
                }
            } catch (Exception e) {
                if (clip != null) {
                    clip = null;
                }
                Utility.error("Error playing the wav: '" + path + "'", "WaveCore.play()", e);
            }
        }
