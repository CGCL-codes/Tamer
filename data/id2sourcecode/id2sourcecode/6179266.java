    private void print(final LightCueDetail detail) {
        out.print("  submasters: ");
        for (int i = 0; i < detail.getNumberOfSubmasters(); i++) {
            out.print(i + 1);
            CueSubmasterLevel level = detail.getSubmasterLevel(i);
            print(level.getIntValue(), level.isDerived());
        }
        out.println("");
        out.print("  channels:   ");
        for (int i = 0; i < detail.getNumberOfChannels(); i++) {
            out.print(i + 1);
            CueChannelLevel level = detail.getChannelLevel(i);
            print(level.getChannelIntValue(), level.isDerived());
        }
        out.println("");
    }
