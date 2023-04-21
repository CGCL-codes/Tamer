    public AudioFormat[] getTargetFormats(Encoding targetEncoding, AudioFormat sourceFormat) {
        if (AudioFloatConverter.getConverter(sourceFormat) == null) return new AudioFormat[0];
        int channels = sourceFormat.getChannels();
        ArrayList<AudioFormat> formats = new ArrayList<AudioFormat>();
        if (targetEncoding.equals(Encoding.PCM_SIGNED)) formats.add(new AudioFormat(Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, 8, channels, channels, AudioSystem.NOT_SPECIFIED, false));
        if (targetEncoding.equals(Encoding.PCM_UNSIGNED)) formats.add(new AudioFormat(Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, 8, channels, channels, AudioSystem.NOT_SPECIFIED, false));
        for (int bits = 16; bits < 32; bits += 8) {
            if (targetEncoding.equals(Encoding.PCM_SIGNED)) {
                formats.add(new AudioFormat(Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, bits, channels, channels * bits / 8, AudioSystem.NOT_SPECIFIED, false));
                formats.add(new AudioFormat(Encoding.PCM_SIGNED, AudioSystem.NOT_SPECIFIED, bits, channels, channels * bits / 8, AudioSystem.NOT_SPECIFIED, true));
            }
            if (targetEncoding.equals(Encoding.PCM_UNSIGNED)) {
                formats.add(new AudioFormat(Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, bits, channels, channels * bits / 8, AudioSystem.NOT_SPECIFIED, true));
                formats.add(new AudioFormat(Encoding.PCM_UNSIGNED, AudioSystem.NOT_SPECIFIED, bits, channels, channels * bits / 8, AudioSystem.NOT_SPECIFIED, false));
            }
        }
        if (targetEncoding.equals(AudioFloatConverter.PCM_FLOAT)) {
            formats.add(new AudioFormat(AudioFloatConverter.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 32, channels, channels * 4, AudioSystem.NOT_SPECIFIED, false));
            formats.add(new AudioFormat(AudioFloatConverter.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 32, channels, channels * 4, AudioSystem.NOT_SPECIFIED, true));
            formats.add(new AudioFormat(AudioFloatConverter.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 64, channels, channels * 8, AudioSystem.NOT_SPECIFIED, false));
            formats.add(new AudioFormat(AudioFloatConverter.PCM_FLOAT, AudioSystem.NOT_SPECIFIED, 64, channels, channels * 8, AudioSystem.NOT_SPECIFIED, true));
        }
        return formats.toArray(new AudioFormat[formats.size()]);
    }
