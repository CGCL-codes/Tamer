    protected void setChannel(String name) {
        try {
            pvWheel.removePropertyChangeListener("value", wheelListener);
            theChannel = ChannelFactory.defaultFactory().getChannel(name);
            setLimits(theChannel);
            updateWheelListener();
            pvWheel.addPropertyChangeListener("value", wheelListener);
        } catch (Exception exc) {
            theChannel = null;
            System.out.println("cannot make channel named " + name);
        }
    }
