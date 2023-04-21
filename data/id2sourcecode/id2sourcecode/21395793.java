    private void parseCue() {
        String number = getString("number");
        String page = getString("page");
        String prompt = "";
        String description = getString("description");
        cue = new Cue(dirty, number, page, prompt, description);
        new CueDetailFactory(show.getNumberOfChannels(), show.getNumberOfSubmasters()).update(cue);
        show.getCues().add(cue);
        if (cue.isLightCue()) {
            lightCueDetail = (LightCueDetail) cue.getDetail();
            for (int i = 0; i < lightCueDetail.getNumberOfSubmasters(); i++) {
                lightCueDetail.getSubmasterLevel(i).getLevelValue().setActive(false);
                lightCueDetail.getSubmasterLevel(i).setDerived(false);
            }
            for (int i = 0; i < lightCueDetail.getNumberOfChannels(); i++) {
                lightCueDetail.getChannelLevel(i).getChannelLevelValue().setActive(false);
                lightCueDetail.getChannelLevel(i).setDerived(false);
                lightCueDetail.getChannelLevel(i).getSubmasterLevelValue().setActive(false);
            }
        }
    }
