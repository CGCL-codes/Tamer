    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1 * n + s1.getLength() - l1, 0);
        ch1.getChannel().markChange();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l1; j++) {
                tmp.set(o1 + i * l1 + j, s1.get(o1 + j));
            }
        }
        tmp.copy(s1, 0, 0, o1);
        tmp.copy(s1, o1 + l1, o1 + l1 + l1 * (n - 1), s1.getLength() - o1 - l1);
        for (int i = 0; i < n; i++) {
            AOToolkit.applyZeroCross(tmp, o1 + l1 * (n + 1));
        }
        ch1.getChannel().setSamples(tmp);
    }
