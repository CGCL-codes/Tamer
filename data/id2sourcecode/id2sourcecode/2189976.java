    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray s1 = ch1.getChannel().getSamples();
        MMArray s2 = ch2.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        switch(operation) {
            case ADD:
                AOToolkit.add(s1, s2, o1, l1);
                break;
            case SUBTRACT:
                AOToolkit.subtract(s1, s2, o1, l1);
                break;
            case MULTIPLY:
                AOToolkit.multiply(s1, s2, o1, l1);
                break;
            case DIVIDE:
                AOToolkit.divide(s1, s2, o1, l1);
                break;
        }
    }
