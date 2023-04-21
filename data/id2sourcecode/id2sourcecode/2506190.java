        public void channelLevelChanged(final int cueIndex, final int channelIndex) {
            Cues cues = context.getShow().getCues();
            int current = cues.getCurrentIndex();
            if (cueIndex == current) {
                Cue cueDefinition = cues.getCurrentCue();
                if (cueDefinition.isLightCue()) {
                    LightCues lightCues = cues.getLightCues();
                    int lightCueIndex = cueDefinition.getLightCueIndex();
                    LightCueDetail detail = lightCues.getDetail(lightCueIndex);
                    float value = detail.getChannelLevel(channelIndex).getValue();
                    int dmxValue = Dmx.getDmxValue(value);
                    channelChangeProcessor.change(Lanbox.ENGINE_SHEET, new ChannelChange(channelIndex + 1, dmxValue));
                }
            }
        }
