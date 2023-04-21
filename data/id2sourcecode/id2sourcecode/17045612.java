    private void initParams(AudioFormat sourceFormat, int bitRate, int channelMode, int quality, boolean VBR) {
        if (sourceFormat.getSampleRate() < 32000 && bitRate > 160) {
            bitRate = 160;
        }
        if (TDebug.TraceAudioConverter) {
            TDebug.out("LAME parameters: channels=" + sourceFormat.getChannels() + "  sample rate=" + ((int) Math.round(sourceFormat.getSampleRate()) + "Hz") + "  bitrate=" + bitRate + "KBit/s");
            TDebug.out("                 channelMode=" + chmode2string(channelMode) + "   quality=" + quality2string(quality) + "   VBR=" + VBR + "  bigEndian=" + sourceFormat.isBigEndian());
        }
        int result = nInitParams(sourceFormat.getChannels(), (int) Math.round(sourceFormat.getSampleRate()), bitRate, channelMode, quality, VBR, sourceFormat.isBigEndian());
        if (result < 0) {
            handleNativeException(result);
            throw new IllegalArgumentException("parameters not supported by LAME (returned " + result + ")");
        }
        try {
            System.setProperty(PROPERTY_PREFIX + "effective.quality", quality2string(getEffectiveQuality()));
            System.setProperty(PROPERTY_PREFIX + "effective.bitrate", String.valueOf(getEffectiveBitRate()));
            System.setProperty(PROPERTY_PREFIX + "effective.chmode", chmode2string(getEffectiveChannelMode()));
            System.setProperty(PROPERTY_PREFIX + "effective.vbr", String.valueOf(getEffectiveVBR()));
            System.setProperty(PROPERTY_PREFIX + "effective.samplerate", String.valueOf(getEffectiveSampleRate()));
            System.setProperty(PROPERTY_PREFIX + "effective.encoding", getEffectiveEncoding().toString());
            System.setProperty(PROPERTY_PREFIX + "encoder.version", getEncoderVersion());
        } catch (Throwable t) {
            if (TDebug.TraceAllExceptions) {
                TDebug.out(t);
            }
        }
    }
