    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(Math.max(2, s1.getLength() - l1), 0);
        ch1.getChannel().markChange();
        try {
            tmp.copy(s1, 0, 0, o1);
            tmp.copy(s1, o1 + l1, o1, tmp.getLength() - o1);
            AOToolkit.applyZeroCross(tmp, o1);
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
        ch1.getChannel().setSamples(tmp);
    }
