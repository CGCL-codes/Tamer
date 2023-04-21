    public Format setInputFormat(Format format) {
        if (super.setInputFormat(format) != null) {
            AudioFormat af = (AudioFormat) format;
            outputFormat = new WavAudioFormat(AudioFormat.LINEAR, af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSizeInBits(), (int) (af.getFrameSizeInBits() * af.getSampleRate() / 8), af.getEndian(), af.getSigned(), (float) af.getFrameRate(), af.getDataType(), new byte[0]);
            return format;
        } else {
            return null;
        }
    }
