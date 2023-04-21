    public int compare(Object a, Object b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        final AudioFormat aCast = (AudioFormat) a;
        final AudioFormat bCast = (AudioFormat) b;
        if (aCast.getSampleRate() > bCast.getSampleRate()) return 1; else if (aCast.getSampleRate() < bCast.getSampleRate()) return -1;
        if (aCast.getChannels() > bCast.getChannels()) return 1; else if (aCast.getChannels() < bCast.getChannels()) return -1;
        if (aCast.getSampleSizeInBits() > bCast.getSampleSizeInBits()) return 1; else if (aCast.getSampleSizeInBits() < bCast.getSampleSizeInBits()) return -1;
        return 0;
    }
