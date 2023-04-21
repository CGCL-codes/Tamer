        public static AnalogInput getChannelForIndex(int channelNo) {
            switch(channelNo) {
                case 1:
                    return CHANNEL1;
                case 2:
                    return CHANNEL2;
                default:
                    throw new IllegalArgumentException();
            }
        }
