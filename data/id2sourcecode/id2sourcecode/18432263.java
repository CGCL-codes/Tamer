    private boolean performHorizontal(float a, MMArray re, MMArray im, int x, int length, AChannel2DSelection chs) {
        boolean changed = false;
        float sr = chs.getChannel().getParentClip().getSampleRate();
        float sw = 1 << (chs.getChannel().getParentClip().getSampleWidth() - 1);
        if (avh == null) {
            avh = new float[length];
        }
        for (int i = 0; i < length; i++) {
            avh[i] = AOToolkit.movingAverage(avh[i], AOToolkit.cartesianToMagnitude(re.get(i), im.get(i)) / sw, 100);
        }
        for (int j = 0; j < length; j++) {
            if (chs.getArea().isSelected(x, (float) j * sr / length / 2)) {
                {
                    float f = (float) Math.pow(avh[j], a);
                    re.set(j, re.get(j) * f);
                    im.set(j, im.get(j) * f);
                    changed = true;
                }
            }
        }
        return changed;
    }
