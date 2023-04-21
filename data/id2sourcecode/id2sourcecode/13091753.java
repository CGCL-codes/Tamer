        public void receiveSample(RawSample rawSample) {
            System.out.println(name);
            int packetNumber = rawSample.getPacketNumber();
            System.out.println(" Packet number:" + packetNumber);
            for (int i = 0, length = rawSample.getChannelNumbers().length; i < length; i++) {
                int channelNumber = rawSample.getChannelNumbers()[i];
                int sample = rawSample.getSamples()[i];
                System.out.println("  Channel #" + channelNumber + " = " + sample);
            }
        }
