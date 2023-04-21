    private void loadDataLine() throws LineUnavailableException, AudioInputException {
        AudioFormat format = in.getFormat();
        if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
            AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
            in = AudioSystem.getAudioInputStream(tmp, in);
            format = tmp;
        } else if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED && format.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED) {
            throw new AudioInputException("Unexpected AudioFormat " + format.getEncoding());
        }
        int size = (int) in.getFrameLength() * format.getFrameSize();
        DataLine.Info info = new DataLine.Info(Clip.class, format, size);
        line = (Clip) AudioSystem.getLine(info);
    }
