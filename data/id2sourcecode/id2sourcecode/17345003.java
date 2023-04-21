    public void setTuning(StringTuning tuning) {
        strings = new java.util.ArrayList<TunedString>();
        for (int i = 0; i < tuning.getPolyphony(); i++) {
            strings.add(new TunedString(i, outPort.getChannelWriter(i)));
        }
        this.tuning = tuning;
    }
