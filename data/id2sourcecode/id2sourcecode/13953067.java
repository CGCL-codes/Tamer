    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray sample = ch1.getChannel().getSamples();
        MMArray room = ch2.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        class Echo {

            public Echo(int d, float g) {
                delay = d;
                gain = g;
            }

            public int delay;

            public float gain;
        }
        ArrayList<Echo> echoList = new ArrayList<Echo>(333);
        for (int i = 0; i < room.getLength(); i++) {
            if (room.get(i) != 0) {
                echoList.add(new Echo(i, room.get(i)));
            }
        }
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                for (int j = 0; j < echoList.size(); j++) {
                    int d = echoList.get(j).delay;
                    float g = echoList.get(j).gain;
                    if (continueWet || i + d < l1) {
                        tmp.set(i + d, tmp.get(i + d) + sample.get(i + o1) * g);
                    }
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
            for (int i = 0; i < tmp.getLength(); i++) {
                float s = dry * sample.get(i + o1) + wet * tmp.get(i);
                sample.set(i + o1, ch1.mixIntensity(i + o1, sample.get(i + o1), s));
            }
            AOToolkit.applyZeroCross(sample, o1);
            AOToolkit.applyZeroCross(sample, o1 + tmp.getLength());
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
