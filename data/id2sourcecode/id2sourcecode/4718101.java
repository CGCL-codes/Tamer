    public static String format2ShortStr(AudioFormat format) {
        return format.getEncoding() + "-" + NS_or_number(format.getChannels()) + "ch-" + NS_or_number(format.getSampleSizeInBits()) + "bit-" + NS_or_number(((int) format.getSampleRate())) + "Hz-" + (format.isBigEndian() ? "be" : "le");
    }
