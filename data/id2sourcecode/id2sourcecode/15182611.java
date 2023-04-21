    protected int getChannel1() {
        int[] values = new int[1];
        execute(new ChannelReadData(values, 1, 1, values.length));
        return values[0];
    }
