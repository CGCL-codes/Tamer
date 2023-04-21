    public Format[] getSupportedOutputFormats(Format input) {
        if (input == null) {
            return new Format[] { new AudioFormat(AudioFormat.LINEAR) };
        }
        if (input instanceof AudioFormat) {
            AudioFormat af = (AudioFormat) input;
            int ssize = af.getSampleSizeInBits();
            int chnl = af.getChannels();
            int endian = af.getEndian();
            int signed = af.getSigned();
            outputFormats = new Format[] { new AudioFormat(AudioFormat.LINEAR, 8000.0, ssize, chnl, endian, signed), new AudioFormat(AudioFormat.LINEAR, 11025.0, ssize, chnl, endian, signed), new AudioFormat(AudioFormat.LINEAR, 16000.0, ssize, chnl, endian, signed), new AudioFormat(AudioFormat.LINEAR, 22050.0, ssize, chnl, endian, signed), new AudioFormat(AudioFormat.LINEAR, 32000.0, ssize, chnl, endian, signed), new AudioFormat(AudioFormat.LINEAR, 44100.0, ssize, chnl, endian, signed), new AudioFormat(AudioFormat.LINEAR, 48000.0, ssize, chnl, endian, signed) };
        } else {
            outputFormats = new Format[0];
        }
        return outputFormats;
    }
