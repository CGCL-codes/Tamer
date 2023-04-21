    private void writeLightCueChannels(final LightCueDetail detail) throws IOException {
        out.openElement("scene");
        for (int i = 0; i < detail.getNumberOfChannels(); i++) {
            CueChannelLevel level = detail.getChannelLevel(i);
            if (level.getChannelLevelValue().isActive()) {
                out.openElement("set");
                out.addAttribute("fixture-id", i + 1);
                String value = level.isDerived() ? "derived" : "" + level.getChannelIntValue();
                out.addAttribute("attribute", "Intensity");
                out.addAttribute("value", value);
                out.closeElement();
            }
        }
        out.closeElement();
    }
