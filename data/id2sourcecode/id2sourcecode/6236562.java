    private void fade1(final int mode, final Time holdTime) {
        FadeType fadeType = null;
        Time fadeTime = null;
        if (mode == IN) {
            fadeType = SPLIT_FADE_IN;
            fadeTime = timing.getFadeInTime();
        } else {
            fadeType = SPLIT_FADE_OUT;
            fadeTime = timing.getFadeOutTime();
        }
        cueSteps.add(new CueScene(fadeType, fadeTime, holdTime, detail.getChannelChanges()));
    }
