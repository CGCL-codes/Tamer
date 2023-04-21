    private boolean performVertical(float a, MMArray re, MMArray im, int x, int length, AChannel2DSelection chs) {
        boolean changed = false;
        float sr = chs.getChannel().getParentClip().getSampleRate();
        float sw = 1 << (chs.getChannel().getParentClip().getSampleWidth() - 1);
        float xm[] = new float[length];
        for (int i = 0; i < length; i++) {
            xm[i] = AOToolkit.cartesianToMagnitude(re.get(i), im.get(i));
        }
        float av = AOToolkit.average(xm, 0, length) / sw;
        if (Math.abs(av) > Math.abs(avv)) {
            avv = av;
        } else {
            avv = Math.max(av, av * decay);
        }
        System.out.println("avv=" + avv);
        float f = (float) Math.pow(avv, a);
        for (int j = 0; j < length; j++) {
            if (chs.getArea().isSelected(x, (float) j * sr / length / 2)) {
                {
                    re.set(j, re.get(j) * f);
                    im.set(j, im.get(j) * f);
                    changed = true;
                }
            }
        }
        return changed;
    }
