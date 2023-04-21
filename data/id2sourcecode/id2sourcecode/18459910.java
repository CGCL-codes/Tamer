    public final void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray mag = ch1.getChannel().getSamples();
        MMArray phas = ch2.getChannel().getSamples();
        ch1.getChannel().markChange();
        int l = 1 << (int) Math.ceil(Math.log(mag.getLength()) / Math.log(2));
        MMArray re = new MMArray(2 * l, 0);
        MMArray im = new MMArray(2 * l, 0);
        try {
            for (int i = 0; i < l; i++) {
                float m = 0;
                float p = 0;
                if (i < mag.getLength()) {
                    m = mag.get(i);
                }
                if (i < phas.getLength()) {
                    p = phas.get(i);
                }
                re.set(i, AOToolkit.polarToX(m, p));
                im.set(i, AOToolkit.polarToY(m, p));
            }
            AOToolkit.complexIfft(re, im);
            ch1.getChannel().setSamples(re);
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
