    private static AudioFormat setUnspecifiedFieldsFromProto(AudioFormat incomplete, AudioFormat prototype) {
        AudioFormat format = new AudioFormat(incomplete.getEncoding(), getSpecificValue(incomplete.getSampleRate(), prototype.getSampleRate()), getSpecificValue(incomplete.getSampleSizeInBits(), prototype.getSampleSizeInBits()), getSpecificValue(incomplete.getChannels(), prototype.getChannels()), getSpecificValue(incomplete.getFrameSize(), prototype.getFrameSize()), getSpecificValue(incomplete.getFrameRate(), prototype.getFrameRate()), incomplete.isBigEndian());
        return format;
    }
