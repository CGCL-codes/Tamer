    public static long audioTimeToLen(long duration, AudioFormat af) {
        long units, bytesPerSec;
        if (af.getSampleSizeInBits() > 0) {
            units = (af.getSampleSizeInBits() * af.getChannels());
            bytesPerSec = (long) ((units * af.getSampleRate()) / 8);
        } else if (af.getFrameSizeInBits() != Format.NOT_SPECIFIED && af.getFrameRate() != Format.NOT_SPECIFIED) {
            units = af.getFrameSizeInBits();
            bytesPerSec = (long) ((units * af.getFrameRate()) / 8);
        } else {
            units = bytesPerSec = 0;
        }
        return (bytesPerSec == 0 ? 0 : ((duration * bytesPerSec) / 1000000000) / units * units);
    }
