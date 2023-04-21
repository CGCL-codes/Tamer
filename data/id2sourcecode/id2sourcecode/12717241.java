    public void unalign(String chanHandle) throws NoSuchChannelException {
        Channel channel = channelSuite.getChannel(chanHandle);
        if (channel == null) {
            throw new NoSuchChannelException(this, chanHandle);
        }
        if (chanHandle.equals("xAvg")) {
            OffsetTransform aTransform = new OffsetTransform(0);
            System.out.println("reset alignment x for " + chanHandle);
            channel.setValueTransform(aTransform);
        } else if (chanHandle.equals("yAvg")) {
            OffsetTransform aTransform = new OffsetTransform(0);
            System.out.println("reset alignment y for " + chanHandle);
            channel.setValueTransform(aTransform);
        }
    }
