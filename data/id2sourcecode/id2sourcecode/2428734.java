        private final void doFloatConversion(FloatSampleBuffer buffer, boolean expandChannels) {
            if (needMixDown) {
                buffer.mixDownChannels();
            }
            if (expandChannels) {
                buffer.expandChannel(getFormat().getChannels());
            }
        }
