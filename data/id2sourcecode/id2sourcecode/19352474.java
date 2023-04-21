    public void caput() throws ConnectionException, PutException {
        Channel channel = ChannelFactory.defaultFactory().getChannel(ch);
        channel.connectAndWait(1000);
        Double newVal = (Double) ((SpinnerNumberModel) this.getModel()).getValue();
        channel.putVal(newVal.doubleValue());
    }
