    public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioFloatInputStream sourceStream) {
        if (!isConversionSupported(targetFormat, sourceStream.getFormat())) throw new IllegalArgumentException("Unsupported conversion: " + sourceStream.getFormat().toString() + " to " + targetFormat.toString());
        if (targetFormat.getChannels() != sourceStream.getFormat().getChannels()) sourceStream = new AudioFloatInputStreamChannelMixer(sourceStream, targetFormat.getChannels());
        if (Math.abs(targetFormat.getSampleRate() - sourceStream.getFormat().getSampleRate()) > 0.000001) sourceStream = new AudioFloatInputStreamResampler(sourceStream, targetFormat);
        return new AudioInputStream(new AudioFloatFormatConverterInputStream(targetFormat, sourceStream), targetFormat, sourceStream.getFrameLength());
    }
