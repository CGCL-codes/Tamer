    public PortHandler getChannelLatchPort() {
        return new PortHandler() {

            public short read() {
                return (short) getChannel();
            }

            public void write(short value) {
                setChannel(value);
            }
        };
    }
