    private void write(final Show show, final Cue cue) throws ShowFileException {
        write("CUE");
        writeQuoted(cue.getNumber());
        writeQuoted(cue.getPage());
        writeQuoted(cue.getPrompt());
        writeQuoted(cue.getDescription());
        if (cue.isLightCue()) {
            int lightCueIndex = cue.getLightCueIndex();
            LightCueDetail detail = show.getCues().getLightCues().getDetail(lightCueIndex);
            for (int i = 0; i < detail.getNumberOfSubmasters(); i++) {
                CueSubmasterLevel level = detail.getSubmasterLevel(i);
                if (level.getLevelValue().isActive()) {
                    if (level.isDerived()) {
                        write(" x");
                    } else {
                        write(" " + level.getIntValue());
                    }
                } else {
                    write(" -");
                }
            }
            for (int i = 0; i < detail.getNumberOfChannels(); i++) {
                CueChannelLevel level = detail.getChannelLevel(i);
                if (level.getChannelLevelValue().isActive()) {
                    if (level.isDerived()) {
                        write(" x");
                    } else {
                        write(" " + level.getChannelIntValue());
                    }
                } else {
                    write(" -");
                }
            }
        }
        writeln("");
    }
