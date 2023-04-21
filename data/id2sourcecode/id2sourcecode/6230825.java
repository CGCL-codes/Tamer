    @Override
    public Format[] getSupportedOutputFormats(Format input) {
        if (input == null) return outputFormats; else {
            if (!(input instanceof AudioFormat)) {
                return new Format[] { null };
            }
            final AudioFormat inputCast = (AudioFormat) input;
            if (!inputCast.getEncoding().equals(AudioFormat.GSM_RTP)) {
                return new Format[] { null };
            }
            final AudioFormat result = new AudioFormat(AudioFormat.GSM, inputCast.getSampleRate(), inputCast.getSampleSizeInBits(), inputCast.getChannels(), inputCast.getEndian(), inputCast.getSigned(), inputCast.getFrameSizeInBits(), inputCast.getFrameRate(), inputCast.getDataType());
            return new Format[] { result };
        }
    }
