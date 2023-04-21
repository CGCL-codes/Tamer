    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        MMArray tmp = new MMArray(s1.getLength() + insertedLength, 0);
        ch1.getChannel().markChange();
        tmp.copy(s1, 0, 0, o1);
        for (int i = o1; i < o1 + insertedLength; i++) {
            tmp.set(i, 0);
        }
        tmp.copy(s1, o1, o1 + insertedLength, tmp.getLength() - o1 - insertedLength);
        AOToolkit.applyZeroCross(tmp, o1);
        AOToolkit.applyZeroCross(tmp, o1 + insertedLength);
        ch1.getChannel().setSamples(tmp);
    }
