    private void reverse(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        try {
            tmp.copy(s1, o1, 0, l1);
            for (int i = 0; i < l1; i++) s1.set(i + o1, tmp.get(l1 - i - 1));
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
