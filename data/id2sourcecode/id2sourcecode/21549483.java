    public void run() throws InterruptedException {
        SensorConnection sonic = null;
        try {
            sonic = (SensorConnection) Connector.open("sensor:proximity");
        } catch (IOException e) {
            System.err.println("No such sensor");
            Button.waitForAnyPress();
            System.exit(1);
        }
        SensorInfo sonicInfo = sonic.getSensorInfo();
        Condition condition = new LimitCondition(100, Condition.OP_LESS_THAN);
        ChannelInfo channelInfo = sonic.getSensorInfo().getChannelInfos()[0];
        System.out.println("Got channelInfo: " + (channelInfo == null ? "null" : channelInfo));
        Channel channel = sonic.getChannel(channelInfo);
        System.out.println("Got channel: " + (channel == null ? "null" : channel));
        channel.addCondition(this, condition);
        System.out.println("Added condition");
        pilot.forward();
        while (pilot.isMoving()) {
            try {
                Data[] data = sonic.getData(1);
                System.out.println("Range = " + data[0].getIntValues()[0]);
                Thread.sleep(100);
            } catch (IOException ioe) {
                System.err.println("Failed to read sensor");
            }
        }
        Button.waitForAnyPress();
    }
