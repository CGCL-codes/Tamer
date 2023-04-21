    public static boolean equals(AudioFormat format1, AudioFormat format2) {
        return Encodings.equals(format1.getEncoding(), format2.getEncoding()) && format1.getChannels() == format2.getChannels() && format1.getSampleSizeInBits() == format2.getSampleSizeInBits() && format1.getFrameSize() == format2.getFrameSize() && (Math.abs(format1.getSampleRate() - format2.getSampleRate()) < 1.0e-9) && (Math.abs(format1.getFrameRate() - format2.getFrameRate()) < 1.0e-9);
    }
