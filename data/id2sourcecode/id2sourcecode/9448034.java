    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        MMArray tmp = new MMArray(basePeriod, 0);
        for (int i = 0; i < tmp.getLength(); i++) {
            tmp.set(i, offset);
            for (int j = 0; j < amplitude.length; j++) {
                tmp.set(i, tmp.get(i) + amplitude[j] * (float) Math.sin((double) i / (double) basePeriod * 2 * Math.PI * (j + 1)));
            }
        }
        for (int i = 0; i < l1; i++) {
            float s;
            if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
            if (add) {
                s = s1.get(i + o1) + tmp.get(i % tmp.getLength());
            } else {
                s = tmp.get(i % tmp.getLength());
            }
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
        }
    }
