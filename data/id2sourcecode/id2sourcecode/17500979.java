    public static AudioFloatConverter getConverter(AudioFormat format) {
        AudioFloatConverter conv = null;
        if (format.getFrameSize() == 0) return null;
        if (format.getFrameSize() != ((format.getSampleSizeInBits() + 7) / 8) * format.getChannels()) {
            return null;
        }
        if (format.getEncoding().equals(Encoding.PCM_SIGNED)) {
            if (format.isBigEndian()) {
                if (format.getSampleSizeInBits() <= 8) {
                    conv = new AudioFloatConversion8S();
                } else if (format.getSampleSizeInBits() > 8 && format.getSampleSizeInBits() <= 16) {
                    conv = new AudioFloatConversion16SB();
                } else if (format.getSampleSizeInBits() > 16 && format.getSampleSizeInBits() <= 24) {
                    conv = new AudioFloatConversion24SB();
                } else if (format.getSampleSizeInBits() > 24 && format.getSampleSizeInBits() <= 32) {
                    conv = new AudioFloatConversion32SB();
                } else if (format.getSampleSizeInBits() > 32) {
                    conv = new AudioFloatConversion32xSB(((format.getSampleSizeInBits() + 7) / 8) - 4);
                }
            } else {
                if (format.getSampleSizeInBits() <= 8) {
                    conv = new AudioFloatConversion8S();
                } else if (format.getSampleSizeInBits() > 8 && format.getSampleSizeInBits() <= 16) {
                    conv = new AudioFloatConversion16SL();
                } else if (format.getSampleSizeInBits() > 16 && format.getSampleSizeInBits() <= 24) {
                    conv = new AudioFloatConversion24SL();
                } else if (format.getSampleSizeInBits() > 24 && format.getSampleSizeInBits() <= 32) {
                    conv = new AudioFloatConversion32SL();
                } else if (format.getSampleSizeInBits() > 32) {
                    conv = new AudioFloatConversion32xSL(((format.getSampleSizeInBits() + 7) / 8) - 4);
                }
            }
        } else if (format.getEncoding().equals(Encoding.PCM_UNSIGNED)) {
            if (format.isBigEndian()) {
                if (format.getSampleSizeInBits() <= 8) {
                    conv = new AudioFloatConversion8U();
                } else if (format.getSampleSizeInBits() > 8 && format.getSampleSizeInBits() <= 16) {
                    conv = new AudioFloatConversion16UB();
                } else if (format.getSampleSizeInBits() > 16 && format.getSampleSizeInBits() <= 24) {
                    conv = new AudioFloatConversion24UB();
                } else if (format.getSampleSizeInBits() > 24 && format.getSampleSizeInBits() <= 32) {
                    conv = new AudioFloatConversion32UB();
                } else if (format.getSampleSizeInBits() > 32) {
                    conv = new AudioFloatConversion32xUB(((format.getSampleSizeInBits() + 7) / 8) - 4);
                }
            } else {
                if (format.getSampleSizeInBits() <= 8) {
                    conv = new AudioFloatConversion8U();
                } else if (format.getSampleSizeInBits() > 8 && format.getSampleSizeInBits() <= 16) {
                    conv = new AudioFloatConversion16UL();
                } else if (format.getSampleSizeInBits() > 16 && format.getSampleSizeInBits() <= 24) {
                    conv = new AudioFloatConversion24UL();
                } else if (format.getSampleSizeInBits() > 24 && format.getSampleSizeInBits() <= 32) {
                    conv = new AudioFloatConversion32UL();
                } else if (format.getSampleSizeInBits() > 32) {
                    conv = new AudioFloatConversion32xUL(((format.getSampleSizeInBits() + 7) / 8) - 4);
                }
            }
        } else if (format.getEncoding().equals(PCM_FLOAT)) {
            if (format.getSampleSizeInBits() == 32) {
                if (format.isBigEndian()) conv = new AudioFloatConversion32B(); else conv = new AudioFloatConversion32L();
            } else if (format.getSampleSizeInBits() == 64) {
                if (format.isBigEndian()) conv = new AudioFloatConversion64B(); else conv = new AudioFloatConversion64L();
            }
        }
        if ((format.getEncoding().equals(Encoding.PCM_SIGNED) || format.getEncoding().equals(Encoding.PCM_UNSIGNED)) && (format.getSampleSizeInBits() % 8 != 0)) {
            conv = new AudioFloatLSBFilter(conv, format);
        }
        if (conv != null) conv.format = format;
        return conv;
    }
