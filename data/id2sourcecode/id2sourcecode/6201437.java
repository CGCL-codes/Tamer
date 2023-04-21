    private void calculateWidthMinimumBinaryScaleFactor() {
        int bestFittedWaveformCanvasWidth = this.maxFramePosition + 1;
        this.widthMinimumBinaryScaleFactor = 0;
        while (bestFittedWaveformCanvasWidth > this.insetlessWaveformCanvasWidth) {
            if (bestFittedWaveformCanvasWidth % 2 == 0) {
                bestFittedWaveformCanvasWidth /= 2;
            } else {
                bestFittedWaveformCanvasWidth = (bestFittedWaveformCanvasWidth + 1) / 2;
            }
            this.widthMinimumBinaryScaleFactor--;
        }
    }
