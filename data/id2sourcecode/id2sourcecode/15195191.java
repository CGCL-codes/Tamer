    public final void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray td = ch1.getChannel().getSamples();
        ch1.getChannel().markChange();
        ch2.getChannel().markChange();
        try {
            int l = 1 << (int) Math.ceil(Math.log(td.getLength()) / Math.log(2));
            MMArray re = new MMArray(l, 0);
            MMArray im = new MMArray(l, 0);
            for (int i = 0; i < l; i++) {
                if (i < td.getLength()) {
                    re.set(i, td.get(i));
                    im.set(i, 0);
                } else {
                    re.set(i, 0);
                    im.set(i, 0);
                }
            }
            AOToolkit.complexFft(re, im);
            MMArray mag = new MMArray(l / 2, 0);
            MMArray phas = new MMArray(l / 2, 0);
            for (int i = 0; i < mag.getLength(); i++) {
                mag.set(i, AOToolkit.cartesianToMagnitude(re.get(i), im.get(i)));
                phas.set(i, AOToolkit.cartesianToPhase(re.get(i), im.get(i)));
            }
            ch1.getChannel().setSamples(mag);
            ch2.getChannel().setSamples(phas);
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
