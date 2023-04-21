    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        kernel = ch2.getChannel().getSamples();
        operate(ch1);
    }
