    protected String generateLineInstrument(Line line) {
        int channel = line.getChannel();
        StringBuffer buffer = new StringBuffer();
        buffer.append("kphase line p4, p3, p5\n");
        buffer.append("kline\ttablei kphase, p6, 1\n");
        buffer.append("zkw kline, ").append(channel);
        return buffer.toString();
    }
