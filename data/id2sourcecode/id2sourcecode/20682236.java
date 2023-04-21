    public void startPublishing() {
        super.startPublishing();
        DecoderInputStream decoderStream = new DecoderInputStream(this.decoder);
        AudioFormat format = new AudioFormat(this.decoder.getSampleRate(), this.decoder.getSampleSize(), this.decoder.getChannels(), true, false);
        AudioRecorder recorder = new AudioRecorder(new File("/home/thomas/test.wav"), AudioFileFormat.Type.WAVE, SampleRateConverter.convert(8000.0f, new AudioInputStream(decoderStream, format, AudioSystem.NOT_SPECIFIED)));
    }
