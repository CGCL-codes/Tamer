    private void transparencySliderStateChanged(javax.swing.event.ChangeEvent evt) {
        sliderValue = transparencySlider.getValue();
        alpha = transparencySlider.getValue() * 1.0f / transparencySlider.getMaximum();
        if (maxExtremeReady) {
            channelOutput.getChannelDisplay().setAlpha(1 - alpha, false);
            channelOutput.getOutputDisplayPanel().setAlpha(1 - alpha, true);
            channelOutput.getOutputPreviewPanel().setAlpha(1 - alpha, true);
        }
        if (minExtremeReady) {
            channelOutput.getChannelDisplay().setAlpha(alpha, false);
            channelOutput.getOutputDisplayPanel().setAlpha(alpha, true);
            channelOutput.getOutputPreviewPanel().setAlpha(alpha, true);
        }
        if (sliderValue == 0 && minExtremeReady) {
            flipFocusAndOutputChannels();
            minExtremeReady = false;
            maxExtremeReady = true;
        }
        if (sliderValue == 100 && maxExtremeReady) {
            flipFocusAndOutputChannels();
            maxExtremeReady = false;
            minExtremeReady = true;
        }
    }
