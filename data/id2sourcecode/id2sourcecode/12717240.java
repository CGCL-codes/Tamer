    public void align(String chanHandle) throws NoSuchChannelException {
        Channel channel = channelSuite.getChannel(chanHandle);
        if (channel == null) {
            throw new NoSuchChannelException(this, chanHandle);
        }
        if (chanHandle.equals("xAvg")) {
            OffsetTransform aTransform = new OffsetTransform(getAlign().getX());
            System.out.println("alignment x for " + chanHandle + " = " + getAlign().getX());
            channel.setValueTransform(aTransform);
        } else if (chanHandle.equals("yAvg")) {
            OffsetTransform aTransform = new OffsetTransform(getAlign().getY());
            System.out.println("alignment y for " + chanHandle + " = " + getAlign().getY());
            channel.setValueTransform(aTransform);
        }
    }
