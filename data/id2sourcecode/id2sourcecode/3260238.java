    protected void doExecute(Channel channel) throws CommException {
        read_data = channel.send(write_data);
        int value = read_data[0] & 0xFF;
        try {
            if (value > 0) {
                Util.testModule(value - 1);
            }
            ((MptcChannel) channel).setLastChanged(value);
        } catch (IllegalArgumentException ex) {
            System.err.println("Illegal last changed module: " + value + " (" + read_data[0] + ")");
        }
    }
