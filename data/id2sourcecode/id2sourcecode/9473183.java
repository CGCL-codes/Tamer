    private void mirrorLeft(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(s1.getLength() + l1, 0);
        try {
            tmp.copy(s1, 0, 0, o1);
            for (int i = 0; i < l1; i++) tmp.set(i + o1, s1.get(o1 + l1 - i - 1));
            tmp.copy(s1, o1, o1 + l1, s1.getLength() - o1);
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
        ch1.getChannel().setSamples(tmp);
    }
