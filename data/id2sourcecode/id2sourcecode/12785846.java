    private boolean performMagnitudeAboveThreshold(MMArray re, MMArray im, int x, int length, AChannel2DSelection chs) {
        boolean changed = false;
        float sr = chs.getChannel().getParentClip().getSampleRate();
        for (int j = 0; j < length; j++) {
            if (chs.getArea().isSelected(x, (float) j * sr / length / 2)) {
                if (AOToolkit.cartesianToMagnitude(re.get(j), im.get(j)) > threshold) {
                    re.set(j, re.get(j) * amplification);
                    im.set(j, im.get(j) * amplification);
                    changed = true;
                }
            }
        }
        return changed;
    }
