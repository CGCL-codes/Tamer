    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(Math.max(2, l1), 0);
        ch1.getChannel().markChange();
        try {
            tmp.copy(s1, o1, 0, l1);
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
        ch1.getChannel().setSamples(tmp);
    }
