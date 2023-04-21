    @Override
    public Format[] getSupportedOutputFormats(Format input) {
        if (input == null) return outputFormats; else {
            if (!(input instanceof AudioFormat)) {
                return new Format[] { null };
            }
            final AudioFormat inputCast = (AudioFormat) input;
            if (!inputCast.getEncoding().equals(AudioFormat.GSM) || (inputCast.getSampleSizeInBits() != 8 && inputCast.getSampleSizeInBits() != Format.NOT_SPECIFIED) || (inputCast.getChannels() != 1 && inputCast.getChannels() != Format.NOT_SPECIFIED) || (inputCast.getSigned() != AudioFormat.SIGNED && inputCast.getSigned() != Format.NOT_SPECIFIED) || (inputCast.getFrameSizeInBits() != 264 && inputCast.getFrameSizeInBits() != Format.NOT_SPECIFIED) || (inputCast.getDataType() != null && inputCast.getDataType() != Format.byteArray)) {
                return new Format[] { null };
            }
            final AudioFormat result = new AudioFormat(AudioFormat.LINEAR, inputCast.getSampleRate(), 16, 1, inputCast.getEndian(), AudioFormat.SIGNED, 16, Format.NOT_SPECIFIED, Format.byteArray);
            return new Format[] { result };
        }
    }
