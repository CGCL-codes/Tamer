    public ComboDaSliderPanel(Lab lab, Board aBoard, double min, double max, boolean loopback, Integer... subchannels) {
        theLab = lab;
        this.theBoard = aBoard;
        subChannels = new int[subchannels.length];
        for (int i = 0; i < subchannels.length; ++i) {
            channelsToWatch.add(theBoard.getCommChannel().getChannelName() + "-" + theBoard.getAddress() + "-" + subchannels[i]);
            subChannels[i] = subchannels[i];
        }
        this.min = (int) min * 1000;
        this.max = (int) max * 1000;
        if (min < 0 && max > 0) {
            for (int i = 0; i <= this.max; i = i + STEPPING) {
                lableTable.put(i, new JLabel(((double) i) / 1000 + "V"));
            }
            for (int i = 0; i >= this.min; i = i - STEPPING) {
                lableTable.put(i, new JLabel(((double) i) / 1000 + "V"));
            }
        } else {
            for (int i = this.min; i < this.max; i = i + STEPPING) {
                lableTable.put(i, new JLabel(((double) i) / 1000 + "V"));
            }
        }
        initUI();
    }
