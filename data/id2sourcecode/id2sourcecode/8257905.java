    protected void playSound(URL audiofile) {
        try {
            if (this.clip != null) {
                this.clip.drain();
                this.clip.close();
            }
            AudioInputStream ain = AudioSystem.getAudioInputStream(this.getAudioFile());
            AudioFormat format = ain.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat temp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                ain = AudioSystem.getAudioInputStream(temp, ain);
                format = temp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat(), ((int) ain.getFrameLength() * format.getFrameSize()));
            if (WaveRenderer.mixer != null) {
                this.clip = (Clip) WaveRenderer.mixer.getLine(info);
            } else {
                this.clip = (Clip) AudioSystem.getLine(info);
            }
            this.clip.addLineListener(this);
            this.clip.open(ain);
            this.clip.start();
            if (this.volume != 1.0f) {
                this.setSoundVolume(this.volume);
            }
        } catch (Exception e) {
            this.status = BaseAudioRenderer.ERROR;
            System.err.println("ERROR: Can not playing " + this.getAudioFile() + " caused by: " + e);
        }
    }
