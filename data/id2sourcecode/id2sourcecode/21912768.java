    private void transparencySliderStateChanged(javax.swing.event.ChangeEvent evt) {
        sliderValue = transparencySlider.getValue();
        alpha = sliderValue * 1.0f / transparencySlider.getMaximum();
        ChannelFrame.filterPanel.getChannelDisplay().setAlpha(alpha, false);
        ChannelFrame.filterPanel.getOutputDisplayPanel().setAlpha(alpha, true);
        ChannelFrame.filterPanel.getOutputPreviewPanel().setAlpha(alpha, true);
    }
