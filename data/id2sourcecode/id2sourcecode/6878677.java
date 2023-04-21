    protected void buildModulatorMap(AudioBuffer buffer) {
        int nc = buffer.getChannelCount();
        for (int ch = 0; ch < nc; ch++) {
            if (format.isLFE(ch)) modulatorMap[ch] = -1; else if (!phaseQuad) {
                modulatorMap[ch] = 0;
            } else {
                if (format.isLeft(ch)) modulatorMap[ch] = 0; else if (format.isRight(ch)) modulatorMap[ch] = 1; else modulatorMap[ch] = -1;
            }
        }
    }
