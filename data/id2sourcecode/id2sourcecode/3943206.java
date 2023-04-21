    public ComboAdGaugePanel(Lab lab, Board board, double min, double max, Integer... subchannels) {
        theLab = lab;
        theBoard = board;
        minValue = min;
        maxValue = max;
        gauges = new Gauge[subchannels.length];
        labels = new JLabel[subchannels.length];
        subChannels = new int[subchannels.length];
        for (int i = 0; i < subchannels.length; ++i) {
            channelsToWatch.add(board.getCommChannel().getChannelName() + "-" + board.getAddress() + "-" + subchannels[i]);
            subChannels[i] = subchannels[i];
        }
        this.min = min;
        this.max = max;
        initUI();
    }
